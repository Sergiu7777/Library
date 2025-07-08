package org.sergheimorari.library.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.sergheimorari.library.model.Book;
import org.sergheimorari.library.model.User;
import org.sergheimorari.library.repository.BookRepository;
import org.sergheimorari.library.repository.UserRepository;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

  @Mock private BookRepository bookRepository;
  @Mock private UserRepository userRepository;

  @InjectMocks private BookService bookService;

  private Book book;
  private User user;

  @BeforeEach
  void setUp() {
    user =
        User.builder()
            .id(1L)
            .firstName("Alice")
            .borrowedBooks(new HashSet<>())
            .email("alice.smith@gmail.com")
            .build();

    book =
        Book.builder()
            .id(1L)
            .title("Clean Code")
            .author("Robert C. Martin")
            .isbn("1234567890")
            .amount(2)
            .build();
  }

  @Test
  void addBook_ShouldSaveBook() {
    when(bookRepository.save(book)).thenReturn(book);

    Book savedBook = bookService.addBook(book);

    assertNotNull(savedBook);
    assertEquals("Clean Code", savedBook.getTitle());
    verify(bookRepository).save(book);
  }

  @Test
  void borrowBook_ShouldDecreaseAmountAndAssignBookToUser() {
    when(bookRepository.getBookByIsbnLikeIgnoreCaseOrAuthorLikeIgnoreCaseOrTitleLikeIgnoreCase(
            anyString(), anyString(), anyString()))
        .thenReturn(book);

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Book borrowedBook = bookService.borrowBook("Clean Code", user.getId());

    assertEquals(1, borrowedBook.getAmount());
    assertEquals(user, borrowedBook.getBorrower());
    assertTrue(user.getBorrowedBooks().contains(borrowedBook));

    verify(bookRepository).save(borrowedBook);
    verify(userRepository, times(2))
        .findById(user.getId()); // once for setBorrower and once for addBookToUser
    verify(userRepository).save(user);
  }

  @Test
  void borrowBook_ShouldThrowIfBookNotFound() {
    when(bookRepository.getBookByIsbnLikeIgnoreCaseOrAuthorLikeIgnoreCaseOrTitleLikeIgnoreCase(
            anyString(), anyString(), anyString()))
        .thenReturn(null);

    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> bookService.borrowBook("Nonexistent", 1L));

    assertEquals("Book not found or is not available for borrowing!", exception.getMessage());
  }

  @Test
  void borrowBook_ShouldThrowIfBookIsOutOfStock() {
    book.setAmount(0);
    when(bookRepository.getBookByIsbnLikeIgnoreCaseOrAuthorLikeIgnoreCaseOrTitleLikeIgnoreCase(
            anyString(), anyString(), anyString()))
        .thenReturn(book);

    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> bookService.borrowBook("Clean Code", 1L));

    assertEquals("Book not found or is not available for borrowing!", exception.getMessage());
  }

  @Test
  void returnBook_ShouldIncreaseAmountAndRemoveBorrower() {
    user.getBorrowedBooks().add(book);
    book.setBorrower(user);

    when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
    when(bookRepository.save(any(Book.class))).thenAnswer(invocation -> invocation.getArgument(0));

    Book returnedBook = bookService.returnBook(book, user.getId());

    assertEquals(3, returnedBook.getAmount()); // amount was 2
    assertNull(returnedBook.getBorrower());
    assertFalse(user.getBorrowedBooks().contains(book));

    verify(userRepository).save(user);
    verify(bookRepository).save(book);
  }

  @Test
  void returnBook_ShouldThrowIfUserNotFound() {
    when(userRepository.findById(user.getId())).thenReturn(Optional.empty());

    RuntimeException exception =
        assertThrows(RuntimeException.class, () -> bookService.returnBook(book, user.getId()));

    assertEquals("User not found for userId: " + user.getId(), exception.getMessage());
  }

  @Test
  void listBorrowedBooksByUser_ShouldReturnBooks() {
    Pageable pageable = Pageable.ofSize(2);
    List<Book> borrowed = List.of(book);

    when(bookRepository.findBooksByBorrower_Id(user.getId(), pageable)).thenReturn(borrowed);

    List<Book> result = bookService.listBorrowedBooksByUser(user.getId(), pageable);

    assertEquals(1, result.size());
    assertEquals(book, result.get(0));
  }
}
