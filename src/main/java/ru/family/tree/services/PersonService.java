package ru.family.tree.services;

import ru.family.tree.services.dto.PersonDto;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author scorpion@yandex-team on 31.03.15.
 */
@Path("/persons")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface PersonService {

    @GET
    List<PersonDto> loadAll();

    @GET
    @Path("/{id}")
    PersonDto load(@PathParam("id") long id);

    @PUT
    PersonDto save(PersonDto person);

    @POST
    PersonDto update(PersonDto person);

    @DELETE
    @Path("/{id}")
    void delete(@PathParam("id") long id);

}
