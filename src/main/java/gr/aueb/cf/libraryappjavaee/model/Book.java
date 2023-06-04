package gr.aueb.cf.libraryappjavaee.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter     //In manyToMany relation it seems that there s a conflict when in both side entities @Data is used, in one i side i had to add @Getter, @Setter instead of @Data
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "BOOKS")
public class Book {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "TITLE", unique = true)
    private String title;

    @Column(name = "NUMBER_OF_COPIES")
    private int numberOfCopies;

//    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
//    @JoinColumn(name = "AUTHOR_FK" )
//    private Author author;
    @Column(name = "AUTHOR")
    private String author;

    @JsonIgnore
    @ManyToMany(mappedBy = "rentedBooks", fetch = FetchType.EAGER,cascade = CascadeType.ALL)
    private Set<User> rentByUser = new HashSet<>();

    public void increaseNumberOfCopies(){
        numberOfCopies++;
    }

    public void addRenter(User user) {
        for (User u : rentByUser) {
            if (u.getUsername().equals(user.getUsername())) {
                return;
            }
        }
        this.rentByUser.add(user);
        user.addBook(this);
    }

    public void removeRenter(User user) {
        for (User u : rentByUser) {
            if (u.getUsername().equals(user.getUsername())) {
                rentByUser.remove(user);
                user.removeBook(this);
                break;
            }
        }
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", numberOfCopies=" + numberOfCopies +
                ", author='" + author + '\'' +
                '}';
    }
}
