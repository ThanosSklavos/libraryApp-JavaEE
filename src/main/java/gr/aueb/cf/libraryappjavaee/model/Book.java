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
@Table(name = "BOOKS")
public class Book {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "TITLE")
    private String title;

    @Column(name = "NUMBER_OF_COPIES")
    private int numberOfCopies;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "AUTHOR_FK", nullable = false)
    private Author author;

    @ManyToMany(mappedBy = "rentedBooks")
    private Set<User> rentByUser = new HashSet<>();

    public void decreaseNumberOfCopies() {
        if (numberOfCopies > 0) {
            numberOfCopies--;
        }
    }

    public void increaseNumberOfCopies(){
        numberOfCopies++;
    }

    public void addRenter(User user) {
        this.rentByUser.add(user);

        for (Book book : user.getRentedBooks()) {
            if (book == this) {
                return;
            }
        }
        user.addBook(this);
    }

    public void removeRenter(User user) {
        this.rentByUser.remove(user);
        if (user.getRentedBooks().contains(this)) {
            user.removeBook(this);
        }
    }
}
