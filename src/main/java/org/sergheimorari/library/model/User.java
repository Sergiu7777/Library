package org.sergheimorari.library.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(nullable = false)
  @JdbcTypeCode(SqlTypes.INTEGER)
  private Long id;

  private String firstName;
  private String lastName;

  @NonNull private String email;

  @OneToMany(mappedBy = "borrower", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JsonManagedReference
  private Set<Book> borrowedBooks = new HashSet<>();
}
