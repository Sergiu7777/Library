package org.sergheimorari.library.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.sergheimorari.library.model.Book;
import org.sergheimorari.library.service.BookService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
public class BookController {
  private final BookService bookService;

  @GetMapping("/borrow")
  public ResponseEntity<Book> borrowBook(
      @RequestParam Long userId, @RequestParam String searchString) {
    Book book = bookService.borrowBook(searchString, userId);

    return ResponseEntity.ok(book);
  }

  @PostMapping("/return")
  public ResponseEntity<Book> returnBorrowedBook(
      @RequestBody Book book, @RequestParam Long userId) {
    return ResponseEntity.ok(bookService.returnBook(book, userId));
  }

  @PostMapping
  public ResponseEntity<Book> addBook(@RequestBody Book book) {
    return new ResponseEntity<>(bookService.addBook(book), HttpStatus.CREATED);
  }

  @GetMapping("/my-books")
  public ResponseEntity<List<Book>> listBorrowedBooksByUser(
      @RequestParam Long userId, Pageable pageable) {
    List<Book> borrowedBooks = bookService.listBorrowedBooksByUser(userId, pageable);

    return ResponseEntity.ok(borrowedBooks);
  }
}
