package gr.aueb.cf.libraryappjavaee.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.util.HashSet;
import java.util.Set;


/**
 * Not implemented yet.
 */
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "AUTHORS")
public class Author {
    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "FIRSTNAME")
    private String firstname;

    @Column(name = "LASTNAME")
    private String lastname;

    @OneToMany (mappedBy = "author")
    private Set<Book> books = new HashSet<>();

//    public void addBook(Book book) {
//        this.books.add(book);
//        book.setAuthor(this);
//    }
}
