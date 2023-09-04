package prosense.sassa.srdeft.transmissionfile.boundary;

import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.base.Splitter;

import org.apache.commons.lang3.StringUtils;

import prosense.sassa.srdeft.api.entity.AppException;
import prosense.sassa.srdeft.transmissionfile.entity.TransmissionFile;
import prosense.sassa.srdeft.transmissionfile.control.TransmissionFileAgent;
import prosense.sassa.srdeft.transmissionfile.control.TransmissionFileStore;
import prosense.sassa.srdeft.file.control.TransactionFileStore;

import javax.ejb.Stateless;

import javax.inject.Inject;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Collectors;

@Stateless
@Path("transmissionfiles")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class TransmissionFilesResource {
    @Inject
    TransmissionFileAgent transmissionFileAgent;

    @Inject
    TransmissionFileStore transmissionFileStore;

    @Inject
    TransactionFileStore transactionFileStore;

    @GET
    public Response readTransmissionFiles(@Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
        final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        final Optional<Iterable<String>> filterOptional = Optional.ofNullable(queryParameters.getFirst("_fields")).map(m -> Splitter.on(',').omitEmptyStrings().trimResults().split(m));
        return Response.ok(transmissionFileStore.search(queryParameters).stream().map(transmissionFile -> filterOptional.isPresent() ? transmissionFileAgent.toFilteredMap(transmissionFile, filterOptional.get()) : transmissionFileAgent.toMap(transmissionFile)).collect(Collectors.toCollection(LinkedHashSet::new))).build();
    }

    @POST
    public Response createTransmissionFile(ObjectNode objectNode, @Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
        transmissionFileAgent.validate(objectNode);
        TransmissionFile transmissionFile = transmissionFileAgent.forCreate(objectNode, user);
//         transmissionFileAgent.validateIfExists(transmissionFile);
        transmissionFileAgent.validateCreate(transmissionFile);
        transmissionFile = transmissionFileStore.create(transmissionFile);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(transmissionFile.getId())).build()).entity(transmissionFileAgent.toMap(transmissionFile)).build();
    }

    @GET
    @Path("{id}")
    public Response readTransmissionFile(@PathParam("id") long id, @Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
        final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        final Optional<Iterable<String>> filterOptional = Optional.ofNullable(queryParameters.getFirst("_fields")).map(m -> Splitter.on(',').omitEmptyStrings().trimResults().split(m));
        return Response.ok(filterOptional.isPresent() ? transmissionFileAgent.toFilteredMap(transmissionFileStore.read(id), filterOptional.get()) : transmissionFileAgent.toMap(transmissionFileStore.read(id))).build();
    }

    public TransmissionFile create(TransmissionFile transmissionFile) {
    	if(transmissionFileAgent.exists(transmissionFile))
    		return transmissionFile;
    	else
	        return transmissionFileStore.create(transmissionFile);
    }
}
