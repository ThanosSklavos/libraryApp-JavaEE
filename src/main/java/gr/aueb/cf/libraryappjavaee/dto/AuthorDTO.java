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
public class AuthorDTO {
    private Long id;

    private String firstname;

    private String lastname;

    private Set<Book> books = new HashSet<>();

    //TODO implement author in db
}
