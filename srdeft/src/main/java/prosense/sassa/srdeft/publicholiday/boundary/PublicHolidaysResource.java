package prosense.sassa.srdeft.publicholiday.boundary;

import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.base.Splitter;

import org.apache.commons.lang3.StringUtils;

import prosense.sassa.srdeft.api.boundary.PATCH;
import prosense.sassa.srdeft.api.entity.AppException;
import prosense.sassa.srdeft.publicholiday.entity.PublicHoliday;
import prosense.sassa.srdeft.publicholiday.control.PublicHolidayAgent;
import prosense.sassa.srdeft.publicholiday.control.PublicHolidayStore;

import javax.ejb.Stateless;

import javax.inject.Inject;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Collectors;


@Stateless
@Path("publicholidays")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class PublicHolidaysResource {
    @Inject
    PublicHolidayAgent publicHolidayAgent;

    @Inject
    PublicHolidayStore publicHolidayStore;

    @GET
    public Response readPublicHolidays(@Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
        final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        final Optional<Iterable<String>> filterOptional = Optional.ofNullable(queryParameters.getFirst("_fields")).map(m -> Splitter.on(',').omitEmptyStrings().trimResults().split(m));
        return Response.ok(publicHolidayStore.search(queryParameters).stream().map(publicHoliday -> filterOptional.isPresent() ? publicHolidayAgent.toFilteredMap(publicHoliday, filterOptional.get()) : publicHolidayAgent.toMap(publicHoliday)).collect(Collectors.toCollection(LinkedHashSet::new))).build();
    }

    @POST
    public Response createPublicHoliday(ObjectNode objectNode, @Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
        publicHolidayAgent.validate(objectNode);
        PublicHoliday publicHoliday = publicHolidayAgent.forCreate(objectNode, user);
        publicHolidayAgent.validateCreate(publicHoliday);
        publicHolidayAgent.validateIfDateExists(publicHoliday);
        publicHolidayAgent.validateIfCovJobsExists(publicHoliday);
        publicHoliday = publicHolidayStore.create(publicHoliday);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(publicHoliday.getId())).build()).entity(publicHolidayAgent.toMap(publicHoliday)).build();
    }

    @GET
    @Path("{id}")
    public Response readPublicHoliday(@PathParam("id") long id, @Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
        final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        final Optional<Iterable<String>> filterOptional = Optional.ofNullable(queryParameters.getFirst("_fields")).map(m -> Splitter.on(',').omitEmptyStrings().trimResults().split(m));
        return Response.ok(filterOptional.isPresent() ? publicHolidayAgent.toFilteredMap(publicHolidayStore.read(id), filterOptional.get()) : publicHolidayAgent.toMap(publicHolidayStore.read(id))).build();
    }

    @PATCH
    @Path("{id}")
    public Response updatePublicHoliday(@PathParam("id") long id, ObjectNode objectNode, @Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
        publicHolidayAgent.validate(objectNode);
        PublicHoliday publicHoliday = publicHolidayAgent.forUpdate(publicHolidayStore.read(id), objectNode, user);
        publicHoliday.setId(id);
        publicHolidayAgent.validateUpdate(publicHoliday);
        return Response.ok(publicHolidayAgent.toMap(publicHolidayStore.update(publicHoliday))).build();
    }
    
    @DELETE
    @Path("{id}")
    public void deletePublicHoliday(@PathParam("id") long id, @Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
        publicHolidayStore.delete(id);
    }
}
