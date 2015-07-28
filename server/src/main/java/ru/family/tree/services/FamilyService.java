package ru.family.tree.services;

import ru.family.tree.services.dto.FamilyDto;
import ru.family.tree.services.dto.RelationType;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

/**
 * @author scorpion@yandex-team on 16.04.15.
 */
@Path("/family")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public interface FamilyService {

    @PUT
    FamilyDto create(@QueryParam("owner") long ownerId);

    @GET
    @Path("/{familyId}")
    FamilyDto load(@PathParam("familyId") long familyId);

    @DELETE
    @Path("/{familyId}")
    void remove(@PathParam("familyId") long familyId);

    @POST
    @Path("/add/relation")
    void addRelation(@QueryParam("familyId") long familyId,
                     @QueryParam("person1") long person1,
                     @QueryParam("person2") long person2,
                     @QueryParam("relationType")RelationType relationType);

    @PUT
    @Path("/{familyId}/spouse")
    void addSpouse(@PathParam("familyId") long familyId, @QueryParam("spouse1") long spouse1, @QueryParam("spouse2") long spouse2);

    @DELETE
    @Path("/{familyId}/spouse")
    void removeSpouse(@PathParam("familyId") long familyId, @QueryParam("spouse1") long spouse1, @QueryParam("spouse2") long spouse2);

    @PUT
    @Path("/{familyId}/child")
    void addChild(@PathParam("familyId") long familyId, @QueryParam("parentId") long parentId, @QueryParam("childId") long childId);

    @DELETE
    @Path("/{familyId}/child")
    void removeChild(@PathParam("familyId") long familyId, @QueryParam("parentId") long parentId, @QueryParam("childId") long childId);

}
