package gr.aueb.cf.libraryappjavaee.dto;

import gr.aueb.cf.libraryappjavaee.model.Author;
import gr.aueb.cf.libraryappjavaee.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private Long id;

    private int numberOfCopies;

    private Author author;

    private Set<User> rentByUser = new HashSet<>();
}
