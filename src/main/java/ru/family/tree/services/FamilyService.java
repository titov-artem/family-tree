package ru.family.tree.services;

import ru.family.tree.services.dto.FamilyDto;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * @author scorpion@yandex-team on 16.04.15.
 */
@Path("/family/tree")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface FamilyService {

    @POST
    FamilyDto create(@QueryParam("owner") long ownerId);

    @POST
    @Path("/add/child")
    void addChild(@QueryParam("parentId") long parentId, @QueryParam("childId") long childId);

    @POST
    @Path("/add/spouse")
    void addSpouse(@QueryParam("spouse1") long spouse1, @QueryParam("spouse2") long spouse2);

    @GET
    List<FamilyDto> loadAll();

    @GET
    @Path("{id}")
    FamilyDto load(@PathParam("id") long id);

}
