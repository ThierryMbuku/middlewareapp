package prosense.sassa.srdclient.mqevent.boundary;

import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.base.Splitter;

import org.slf4j.Logger;

import prosense.sassa.srdclient.api.control.App;
import prosense.sassa.srdclient.mqevent.control.MQEventAgent;
import prosense.sassa.srdclient.mqevent.control.MQEventStore;
import prosense.sassa.srdclient.mqevent.entity.MQEvent;

import javax.ejb.Stateless;

import javax.inject.Inject;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
@Path("mqevents")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MQEventsResource {
    @Inject
    @App
    Logger logger;

    @Inject
    MQEventAgent mqeventAgent;

    @Inject
    MQEventStore mqeventStore;

    @GET
    public Response readMQEvents(@Context UriInfo uriInfo) {
        final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        final Optional<Iterable<String>> filterOptional = Optional.ofNullable(queryParameters.getFirst("_fields")).map(m -> Splitter.on(',')
                                                                                                                                    .omitEmptyStrings()
                                                                                                                                    .trimResults()
                                                                                                                                    .split(m));
        return Response.ok(mqeventStore.search(queryParameters)
                                    .stream()
                                    .map(mqevent -> filterOptional.isPresent() ? mqeventAgent.toFilteredMap(mqevent, filterOptional.get()) : mqeventAgent.toMap(mqevent))
                                    .collect(Collectors.toCollection(LinkedHashSet::new))).build();
    }

    @POST
    public Response createMQEvent(ObjectNode objectNode, @Context UriInfo uriInfo) {
        mqeventAgent.validate(objectNode);
        MQEvent mqevent = mqeventAgent.forCreate(objectNode);
        mqeventAgent.validateCreate(mqevent);
        mqevent = mqeventStore.create(mqevent);
        return Response.created(uriInfo.getAbsolutePathBuilder()
                                       .path(String.valueOf(mqevent.getId()))
                                       .build())
                       .entity(mqeventAgent.toMap(mqevent))
                       .build();
    }

    public MQEvent createMQEvent(MQEvent mqevent) {
        mqeventAgent.validateCreate(mqevent);
        mqevent = mqeventStore.create(mqevent);
        return mqevent;
    }

    @GET
    @Path("{id}")
    public Response readMQEvent(@PathParam("id") long id, @Context UriInfo uriInfo) {
        final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        final Optional<Iterable<String>> filterOptional = Optional.ofNullable(queryParameters.getFirst("_fields")).map(m -> Splitter.on(',')
                                                                                                                                    .omitEmptyStrings()
                                                                                                                                    .trimResults()
                                                                                                                                    .split(m));
        return Response.ok(filterOptional.isPresent() ? mqeventAgent.toFilteredMap(mqeventStore.read(id), filterOptional.get()) : mqeventAgent.toMap(mqeventStore.read(id))).build();
    }
}
