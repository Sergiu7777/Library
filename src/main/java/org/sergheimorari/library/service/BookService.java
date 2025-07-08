package org.sergheimorari.library.service;

import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.sergheimorari.library.model.Book;
import org.sergheimorari.library.model.User;
import org.sergheimorari.library.repository.BookRepository;
import org.sergheimorari.library.repository.UserRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class BookService {
  private final BookRepository bookRepository;
  private final UserRepository userRepository;

  public Book addBook(Book book) {
    log.info("Adding book: {}", book);
    return bookRepository.save(book);
  }

  public Book borrowBook(String searchString, Long userId) {
    Book book =
        bookRepository.getBookByIsbnLikeIgnoreCaseOrAuthorLikeIgnoreCaseOrTitleLikeIgnoreCase(
            searchString, searchString, searchString);

    if (book != null && book.getAmount() > 0) {
      log.info("Book found: {}", book);

      book.setAmount(book.getAmount() - 1);
      setBorrowerIfExists(book, userId);
      addBookToUser(userId, book);

      return bookRepository.save(book);
    } else {
      throw new RuntimeException("Book not found or is not available for borrowing!");
    }
  }

  public Book returnBook(Book book, Long userId) {
    removeBookFromUser(userId, book);
    book.setAmount(book.getAmount() + 1);
    book.setBorrower(null);

    return bookRepository.save(book);
  }

  public List<Book> listBorrowedBooksByUser(Long userId, Pageable pageable) {
    return bookRepository.findBooksByBorrower_Id(userId, pageable);
  }

  private void setBorrowerIfExists(Book book, Long userId) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found for userId: " + userId));
    book.setBorrower(user);
  }

  private void removeBookFromUser(Long userId, Book book) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found for userId: " + userId));

    Set<Book> borrowedBooks = user.getBorrowedBooks();
    borrowedBooks.remove(book);
    user.setBorrowedBooks(borrowedBooks);

    userRepository.save(user);
  }

  private void addBookToUser(Long userId, Book book) {
    User user =
        userRepository
            .findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found for userId: " + userId));

    Set<Book> borrowedBooks = user.getBorrowedBooks();
    borrowedBooks.add(book);
    user.setBorrowedBooks(borrowedBooks);

    userRepository.save(user);
  }
}
