package org.sergheimorari.library.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.sergheimorari.library.model.Book;
import org.sergheimorari.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@WebMvcTest(BookController.class)
class BookControllerTest {

  @Autowired private MockMvc mockMvc;

  @MockitoBean private BookService bookService;

  private Book sampleBook;

  @BeforeEach
  void setUp() {
    sampleBook =
        Book.builder()
            .id(1L)
            .title("Clean Code")
            .author("Robert C. Martin")
            .isbn("1234567890")
            .amount(2)
            .build();
  }

  @Test
  void addBook_ShouldReturnCreatedBook() throws Exception {
    when(bookService.addBook(any(Book.class))).thenReturn(sampleBook);

    mockMvc
        .perform(
            post("/api/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(sampleBook)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.title").value("Clean Code"))
        .andExpect(jsonPath("$.amount").value(2));
  }

  @Test
  void borrowBook_ShouldReturnBorrowedBook() throws Exception {
    when(bookService.borrowBook(eq("Clean Code"), eq(1L))).thenReturn(sampleBook);

    mockMvc
        .perform(
            get("/api/books/borrow")
                .param("userId", "1")
                .param(
                    "searchString",
                    "Clean Code")) // issue here: @PathVariable used instead of @RequestParam
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Clean Code"));
  }

  @Test
  void returnBook_ShouldReturnReturnedBook() throws Exception {
    when(bookService.returnBook(any(Book.class), eq(1L))).thenReturn(sampleBook);

    mockMvc
        .perform(
            post("/api/books/return")
                .param("userId", "1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(sampleBook)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("Clean Code"));
  }

  @Test
  void listBorrowedBooksByUser_ShouldReturnBookList() throws Exception {
    when(bookService.listBorrowedBooksByUser(eq(1L), any(Pageable.class)))
        .thenReturn(List.of(sampleBook));

    mockMvc
        .perform(get("/api/books/my-books").param("userId", "1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.length()").value(1))
        .andExpect(jsonPath("$[0].title").value("Clean Code"));
  }
}
