package prosense.sassa.srdeft.covjob.boundary;

import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.base.Splitter;

import org.apache.commons.lang3.StringUtils;

import prosense.sassa.srdeft.api.boundary.PATCH;
import prosense.sassa.srdeft.api.entity.AppException;
import prosense.sassa.srdeft.covjob.entity.CovJob;
import prosense.sassa.srdeft.covjob.control.CovJobAgent;
import prosense.sassa.srdeft.covjob.control.CovJobStore;
import prosense.sassa.srdeft.file.control.TransactionFileStore;

import javax.ejb.Stateless;

import javax.inject.Inject;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Collectors;

import java.time.ZonedDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


@Stateless
@Path("covjobs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CovJobsResource {
    @Inject
    CovJobAgent covJobAgent;

    @Inject
    CovJobStore covJobStore;

    @Inject
    TransactionFileStore transactionFileStore;

    @GET
    public Response readCovJobs(@Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
        final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        final Optional<Iterable<String>> filterOptional = Optional.ofNullable(queryParameters.getFirst("_fields")).map(m -> Splitter.on(',').omitEmptyStrings().trimResults().split(m));
        return Response.ok(covJobStore.search(queryParameters).stream().map(covJob -> filterOptional.isPresent() ? covJobAgent.toFilteredMap(covJob, filterOptional.get()) : covJobAgent.toMap(covJob)).collect(Collectors.toCollection(LinkedHashSet::new))).build();
    }

    @POST
    public Response createCovJob(ObjectNode objectNode, @Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
        covJobAgent.validate(objectNode);
        CovJob covJob = covJobAgent.forCreate(objectNode, user);
        covJobAgent.validateCreate(covJob);
        covJobAgent.validatePayDay(covJob);
        covJobAgent.validateIfExists(covJob, CovJob.Status.pending.name());
        covJobAgent.validateIfExists(covJob, CovJob.Status.started.name());
        covJobAgent.validateIfApprovedOutcomeExists(covJob);
        covJob = covJobStore.create(covJob);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(covJob.getId())).build()).entity(covJobAgent.toMap(covJob)).build();
    }

    @GET
    @Path("{id}")
    public Response readCovJob(@PathParam("id") long id, @Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
        final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        final Optional<Iterable<String>> filterOptional = Optional.ofNullable(queryParameters.getFirst("_fields")).map(m -> Splitter.on(',').omitEmptyStrings().trimResults().split(m));
        return Response.ok(filterOptional.isPresent() ? covJobAgent.toFilteredMap(covJobStore.read(id), filterOptional.get()) : covJobAgent.toMap(covJobStore.read(id))).build();
    }

    @PATCH
    @Path("{id}/sign")
    public Response signCovJob(@PathParam("id") long id, ObjectNode objectNode, @Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
        covJobAgent.validate(objectNode);
        CovJob covJob = covJobAgent.forSign(covJobStore.read(id), objectNode, user);
        covJob.setId(id);
        covJobAgent.validateSign(covJob);
        if (!covJob.getSignStatus().equals(CovJob.SignStatus.approved.name()))
            covJob.setStatus(CovJob.Status.failed.name());
        return Response.ok(covJobAgent.toMap(covJobStore.update(covJob))).build();
    }
    
    @PATCH
    @Path("{id}/start")
    public Response startCovJob(@PathParam("id") long id, ObjectNode objectNode, @Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
        CovJob covJob = covJobAgent.forStart(covJobStore.read(id), objectNode, user);
        covJob.setId(id);
        covJobAgent.validateStart(covJob);
		try {
			covJobAgent.validateIfEodFileExists(covJob);
			covJob.setStatus(CovJob.Status.started.name());
			transactionFileStore.generate(covJob);
		} catch (AppException e) {
			covJob.setEnd(ZonedDateTime.now());
			covJob.setStatus(CovJob.Status.failed.name());
			covJob.setMessages(e.getMessage());
		}
		covJob = covJobStore.update(covJob);
        return Response.ok(covJobAgent.toMap(covJob)).build();
    }
    
    public CovJob create(CovJob covJob) {
        return covJobStore.create(covJob);
    }

    public CovJob update(CovJob covJob) {
        return covJobStore.update(covJob);
    }

    public Set<CovJob> search(CovJob covJob) {
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.add(CovJobAgent.type, covJob.getType());
        queryParameters.add(CovJobAgent.subService, covJob.getSubService());
        queryParameters.add(CovJobAgent.status, covJob.getStatus());
        queryParameters.add(CovJobAgent.test, String.valueOf(covJob.getTest()));
        queryParameters.add("from", ZonedDateTime.now().with(LocalTime.MIN).format(DateTimeFormatter.ISO_INSTANT));
        queryParameters.add("to", ZonedDateTime.now().with(LocalTime.MAX).format(DateTimeFormatter.ISO_INSTANT));
        return covJobStore.search(queryParameters);
    }
}
