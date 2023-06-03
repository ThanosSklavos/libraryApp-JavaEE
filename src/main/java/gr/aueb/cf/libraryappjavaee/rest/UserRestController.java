package gr.aueb.cf.libraryappjavaee.rest;

import gr.aueb.cf.libraryappjavaee.dto.BookDTO;
import gr.aueb.cf.libraryappjavaee.dto.UserDTO;
import gr.aueb.cf.libraryappjavaee.model.Book;
import gr.aueb.cf.libraryappjavaee.model.User;
import gr.aueb.cf.libraryappjavaee.service.IUserService;
import gr.aueb.cf.libraryappjavaee.service.exceptions.EntityAlreadyExistsException;
import gr.aueb.cf.libraryappjavaee.service.exceptions.EntityNotFoundException;
import gr.aueb.cf.libraryappjavaee.service.util.JPAHelper;
import jakarta.persistence.NoResultException;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@Path("/users")
public class UserRestController {

    @Inject
    private IUserService service;

    @Path("/")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUsersByUsername(@QueryParam("username") String username) {
        List<User> users;

        try {
            users = service.getUsersByUsername(username);
            List<UserDTO> userDTOs = new ArrayList<>();
            for (User user : users) {
                userDTOs.add(map(user));
            }
            return Response.status(Response.Status.OK).entity(userDTOs).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("User with username " + username + " was not found").build();
        }
    }

    //TODO why can't it be "/". check the errors.
    @Path("/all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllUsers() {
        List<User> users;

        try {
            users = service.getAllUsers();
            List<UserDTO> userDTOs = new ArrayList<>();
            for (User user : users) {
                userDTOs.add(map(user));
            }
            return Response.status(Response.Status.OK).entity(userDTOs).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("No users found").build();
        }
    }

    @Path("/{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getUserById(@PathParam("id") Long id) {
        User user;
        try {
            user = service.getUserById(id);
            UserDTO userDTO = map(user);
            return Response.status(Response.Status.OK).entity(userDTO).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("User with id " + id + " was not found").build();
        }
    }

    @Path("/")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response addUser(UserDTO dto, @Context UriInfo uriInfo) {
        try {
            User user = service.insert(dto);
            UserDTO userDTO = map(user);
            UriBuilder uriBuilder = uriInfo.getAbsolutePathBuilder();
            return Response.created(uriBuilder.path(Long.toString(userDTO.getId())).build())
                    .entity(userDTO).build();
        } catch (EntityAlreadyExistsException e) {
            return Response
                    .status(Response.Status.BAD_REQUEST)
                    .entity("User already exists")
                    .build();
        }
    }

    @Path("/{id}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("id") Long id) {
        try {
            User user = service.getUserById(id);
            service.delete(id);
            UserDTO userDTO = map(user);
            return Response.status(Response.Status.OK).entity(userDTO).build();
        } catch (EntityNotFoundException e1) {
            return Response
                    .status(Response.Status.NOT_FOUND)
                    .entity("User with id " + id + " was not found")
                    .build();
        }
    }

    @Path("/{id}")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateUser(@PathParam("id") Long id, UserDTO dto) {
        try {
            dto.setId(id);
            User user = service.update(dto);
            UserDTO dtoToReturn = map(user);
            return Response.status(Response.Status.OK).entity(dtoToReturn).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("User with id " + id + " was not found").build();
        }
    }

    private UserDTO map(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPassword(user.getPassword());
        userDTO.setFirstname(user.getFirstname());
        userDTO.setLastname(user.getLastname());
        userDTO.setRentedBooks(user.getRentedBooks());
        return userDTO;
    }

    /* Test features */
    @Path("/validate")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response validateUser(@QueryParam("username") String username,
                                 @QueryParam("password") String password) {
        boolean validated;
        try {
            validated = service.isUserValid(username, password);
            return Response.status(Response.Status.OK).entity("Validated: " + validated).build();
        } catch (EntityNotFoundException | NoResultException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("User with username " + username + " was not found").build();
        }
    }

    //TODO fix this.
    @Path("/{id}/addBook")
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response userAddBook(@PathParam("id") Long id, BookDTO bookDTO) {
        User user;
        try {
            user = service.getUserById(id);
            service.addBook(user, bookDTO);
            user = service.getUserById(id);
            return Response.status(Response.Status.OK).entity(user).build();
        } catch (EntityNotFoundException e) {
            return Response.status(Response.Status.NOT_FOUND).entity("User with id " + id + " was not found").build();
        }
    }

}
