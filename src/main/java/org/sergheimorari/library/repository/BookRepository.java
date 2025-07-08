package org.sergheimorari.library.repository;

import java.util.List;
import org.sergheimorari.library.model.Book;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;

public interface BookRepository extends CrudRepository<Book, Long> {
  Book getBookByIsbn(String isbn);

  Book getBookByIsbnLikeIgnoreCaseOrAuthorLikeIgnoreCaseOrTitleLikeIgnoreCase(
      String isbn, String author, String title);

  List<Book> findBooksByBorrower_Id(Long borrowerId, Pageable pageable);
}
