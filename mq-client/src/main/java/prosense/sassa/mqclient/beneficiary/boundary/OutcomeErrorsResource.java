package prosense.sassa.mqclient.beneficiary.boundary;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.google.common.base.Splitter;

import org.slf4j.Logger;

import prosense.sassa.mqclient.api.control.App;
import prosense.sassa.mqclient.api.entity.AppException;
import prosense.sassa.mqclient.api.boundary.PATCH;
import prosense.sassa.mqclient.beneficiary.control.OutcomeErrorAgent;
import prosense.sassa.mqclient.beneficiary.control.OutcomeErrorStore;
import prosense.sassa.mqclient.beneficiary.entity.OutcomeError;

import javax.ejb.Stateless;

import javax.inject.Inject;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Set;

@Stateless
@Path("outcomeErrors")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class OutcomeErrorsResource {
    @Inject
    @App
    Logger logger;

    @Inject
    OutcomeErrorAgent outcomeErrorAgent;

    @Inject
    OutcomeErrorStore outcomeErrorStore;

    @GET
    public Response readOutcomeErrors(@Context UriInfo uriInfo) {
        final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        final Optional<Iterable<String>> filterOptional = Optional.ofNullable(queryParameters.getFirst("_fields")).map(m -> Splitter.on(',')
                                                                                                                                    .omitEmptyStrings()
                                                                                                                                    .trimResults()
                                                                                                                                    .split(m));
        return Response.ok(outcomeErrorStore.search(queryParameters)
                                    .stream()
                                    .map(outcomeError -> filterOptional.isPresent() ? outcomeErrorAgent.toFilteredMap(outcomeError, filterOptional.get()) : outcomeErrorAgent.toMap(outcomeError))
                                    .collect(Collectors.toCollection(LinkedHashSet::new))).build();
    }

    public Set<OutcomeError> getPendingOutcomes() {
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.add("messageStatus", "pending");
        queryParameters.add("from", ZonedDateTime.now().minusDays(1).format(DateTimeFormatter.ISO_INSTANT));
        queryParameters.add("to", ZonedDateTime.now().plusDays(1).format(DateTimeFormatter.ISO_INSTANT));
        return outcomeErrorStore.search(queryParameters);
    }

    @POST
    public Response createOutcomeError(ObjectNode objectNode, @Context UriInfo uriInfo) {
        outcomeErrorAgent.validate(objectNode);
        OutcomeError outcomeError = outcomeErrorAgent.forCreate(objectNode);
        outcomeError = createOutcomeError(outcomeError);
        return Response.created(uriInfo.getAbsolutePathBuilder()
                                       .path(String.valueOf(outcomeError.getId()))
                                       .build())
                       .entity(outcomeErrorAgent.toMap(outcomeError))
                       .build();
    }

    public OutcomeError createOutcomeError(OutcomeError outcomeError) {
        outcomeErrorAgent.validateCreate(outcomeError);
        outcomeError = outcomeErrorStore.create(outcomeError);
        return outcomeError;
    }

    @PATCH
    @Path("{id}")
    public Response updateOutcomeError(@PathParam("id") long id, ObjectNode objectNode, @Context UriInfo uriInfo) {
        outcomeErrorAgent.validate(objectNode);
        OutcomeError outcomeError = outcomeErrorAgent.forUpdate(outcomeErrorStore.read(id), objectNode);
        outcomeError.setId(id);
        outcomeError = updateOutcomeError(outcomeError);
        return Response.ok(outcomeErrorAgent.toMap(outcomeError)).build();
    }

    public OutcomeError updateOutcomeError(OutcomeError outcomeError) {
        outcomeErrorAgent.validateUpdate(outcomeError);
        outcomeError = outcomeErrorStore.update(outcomeError);
        return outcomeError;
    }
    
    @DELETE
    @Path("{id}")
    public void deleteOutcomeError(@PathParam("id") long id, @Context UriInfo uriInfo) {
        outcomeErrorStore.delete(id);
    }
    
    public void deleteOutcomeError(long id) {
        outcomeErrorStore.delete(id);
    }
    
    @GET
    @Path("{id}")
    public Response readOutcomeError(@PathParam("id") long id, @Context UriInfo uriInfo) {
        final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        final Optional<Iterable<String>> filterOptional = Optional.ofNullable(queryParameters.getFirst("_fields")).map(m -> Splitter.on(',')
                                                                                                                                    .omitEmptyStrings()
                                                                                                                                    .trimResults()
                                                                                                                                    .split(m));
        return Response.ok(filterOptional.isPresent() ? outcomeErrorAgent.toFilteredMap(outcomeErrorStore.read(id), filterOptional.get()) : outcomeErrorAgent.toMap(outcomeErrorStore.read(id))).build();
    }
}
