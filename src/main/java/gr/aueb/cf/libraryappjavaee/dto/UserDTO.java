package gr.aueb.cf.libraryappjavaee.dto;

import gr.aueb.cf.libraryappjavaee.model.Book;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;

    private String username;

    private String firstname;

    private String lastname;

    private String password;

    private Set<Book> rentedBooks = new HashSet<>();

}
