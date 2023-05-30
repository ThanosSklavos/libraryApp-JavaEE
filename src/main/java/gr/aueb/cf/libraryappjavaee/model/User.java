package gr.aueb.cf.libraryappjavaee.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "USERS")
public class User {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "FIRSTNAME")
    private String firstname;

    @Column(name = "LASTNAME")
    private String lastname;

    @Column(name = "PASSWORD")
    private String password;

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "USER_BOOK",
            joinColumns = @JoinColumn(name = "USER_ID", referencedColumnName = "ID"),
            inverseJoinColumns = @JoinColumn(name = "BOOK_ID", referencedColumnName = "ID"))
    private Set<Book> rentedBooks = new HashSet<>();

    public User(String username, String firstname, String lastname, String password) {
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.password = password;
    }

    public void addBook(Book book) {
        this.rentedBooks.add(book);

        for (User user : book.getRentByUser()) {
            if (user == this) {
                return;
            }
        }
        book.addRenter(this);
    }

    public void removeBook(Book book) {
        this.rentedBooks.remove(book);

        if (book.getRentByUser().contains(this)) {
            book.removeRenter(this);
        }
    }
}
