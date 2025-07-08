package org.sergheimorari.library.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  @JdbcTypeCode(SqlTypes.INTEGER)
  private Long id;

  private String title;
  private String author;
  private String isbn;
  private int amount;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User borrower;
}
