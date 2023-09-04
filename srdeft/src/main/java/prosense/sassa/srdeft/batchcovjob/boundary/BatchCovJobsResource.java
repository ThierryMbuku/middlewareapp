package prosense.sassa.srdeft.batchcovjob.boundary;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.node.ObjectNode;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Splitter;

import prosense.sassa.srdeft.api.control.App;
import prosense.sassa.srdeft.api.boundary.PATCH;
import prosense.sassa.srdeft.api.entity.AppException;
import prosense.sassa.srdeft.batchcovjob.entity.BatchCovJob;
import prosense.sassa.srdeft.batchcovjob.control.BatchCovJobAgent;
import prosense.sassa.srdeft.batchcovjob.control.BatchCovJobStore;
import prosense.sassa.srdeft.file.control.TransactionFileStore;
import prosense.sassa.srdeft.file.control.FileAgent;

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
@Path("batchcovjobs")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BatchCovJobsResource {
    @Inject
    @App
    Logger logger;

    @Inject
    BatchCovJobAgent batchCovJobAgent;

    @Inject
    BatchCovJobStore batchCovJobStore;

    @Inject
    TransactionFileStore transactionFileStore;

    @Inject
    FileAgent fileAgent;

    @GET
    public Response readBatchCovJobs(@Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
        final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        final Optional<Iterable<String>> filterOptional = Optional.ofNullable(queryParameters.getFirst("_fields")).map(m -> Splitter.on(',').omitEmptyStrings().trimResults().split(m));
        return Response.ok(batchCovJobStore.search(queryParameters).stream().map(batchCovJob -> filterOptional.isPresent() ? batchCovJobAgent.toFilteredMap(batchCovJob, filterOptional.get()) : batchCovJobAgent.toMap(batchCovJob)).collect(Collectors.toCollection(LinkedHashSet::new))).build();
    }

    @POST
    public Response createBatchCovJob(ObjectNode objectNode, @Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
        batchCovJobAgent.validate(objectNode);
        BatchCovJob batchCovJob = batchCovJobAgent.forCreate(objectNode, user);
        batchCovJobAgent.validateCreate(batchCovJob);
        batchCovJobAgent.validatePayDay(batchCovJob);
        batchCovJobAgent.validateIfExists(batchCovJob, BatchCovJob.Status.pending.name());
        batchCovJobAgent.validateIfExists(batchCovJob, BatchCovJob.Status.started.name());
        batchCovJobAgent.validateIfApprovedOutcomeExists(batchCovJob);
        batchCovJob = batchCovJobStore.create(batchCovJob);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(batchCovJob.getId())).build()).entity(batchCovJobAgent.toMap(batchCovJob)).build();
    }

    @GET
    @Path("{id}")
    public Response readBatchCovJob(@PathParam("id") long id, @Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
        final MultivaluedMap<String, String> queryParameters = uriInfo.getQueryParameters();
        final Optional<Iterable<String>> filterOptional = Optional.ofNullable(queryParameters.getFirst("_fields")).map(m -> Splitter.on(',').omitEmptyStrings().trimResults().split(m));
        return Response.ok(filterOptional.isPresent() ? batchCovJobAgent.toFilteredMap(batchCovJobStore.read(id), filterOptional.get()) : batchCovJobAgent.toMap(batchCovJobStore.read(id))).build();
    }

    @PATCH
    @Path("{id}/sign")
    public Response signBatchCovJob(@PathParam("id") long id, ObjectNode objectNode, @Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
        batchCovJobAgent.validate(objectNode);
        BatchCovJob batchCovJob = batchCovJobAgent.forSign(batchCovJobStore.read(id), objectNode, user);
        batchCovJob.setId(id);
        batchCovJobAgent.validateSign(batchCovJob);
        if (!batchCovJob.getSignStatus().equals(BatchCovJob.SignStatus.approved.name()))
            batchCovJob.setStatus(BatchCovJob.Status.failed.name());
        return Response.ok(batchCovJobAgent.toMap(batchCovJobStore.update(batchCovJob))).build();
    }
    
    @PATCH
    @Path("{id}/start")
    public Response startBatchCovJob(@PathParam("id") long id, ObjectNode objectNode, @Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
        BatchCovJob batchCovJob = batchCovJobAgent.forStart(batchCovJobStore.read(id), objectNode, user);
        batchCovJob.setId(id);
        batchCovJobAgent.validateStart(batchCovJob);
		try {
			batchCovJobAgent.validateIfEodFileExists(batchCovJob);
			batchCovJob.setStatus(BatchCovJob.Status.started.name());
			transactionFileStore.generate(batchCovJob);
		} catch (AppException e) {
			batchCovJob.setEnd(ZonedDateTime.now());
			batchCovJob.setStatus(BatchCovJob.Status.failed.name());
			batchCovJob.setMessages(e.getMessage());
		}
		batchCovJob = batchCovJobStore.update(batchCovJob);
        return Response.ok(batchCovJobAgent.toMap(batchCovJob)).build();
    }
    
    @PATCH
    @Path("{id}/cancel")
    public Response cancelBatchCovJob(@PathParam("id") long id, ObjectNode objectNode, @Context UriInfo uriInfo, @HeaderParam("User") String user) {
        if (StringUtils.isEmpty(user))
            throw AppException.builder().badRequest400().message("user mandatory").build();
        BatchCovJob batchCovJob = batchCovJobAgent.forCancel(batchCovJobStore.read(id), objectNode, user);
        batchCovJob.setId(id);
        batchCovJobAgent.validateCancel(batchCovJob);
        batchCovJob.setStatus(BatchCovJob.Status.cancelled.name());
        return Response.ok(batchCovJobAgent.toMap(batchCovJobStore.update(batchCovJob))).build();
    }
    
    public BatchCovJob update(BatchCovJob batchCovJob) {
        return batchCovJobStore.update(batchCovJob);
    }
    
    public void startScheduledJob() {
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.add(BatchCovJobAgent.processingDate, ZonedDateTime.now().with(LocalTime.MIN).format(DateTimeFormatter.ISO_INSTANT));
        queryParameters.add(BatchCovJobAgent.status, BatchCovJob.Status.pending.name());
        queryParameters.add(BatchCovJobAgent.signStatus, BatchCovJob.SignStatus.approved.name());
        Set<BatchCovJob> batchCovJobs = batchCovJobStore.search(queryParameters);
        logger.info("batchCovJobs size :: " + batchCovJobs.size());
        batchCovJobs.forEach(batchCovJob -> {
			batchCovJob.setStart(ZonedDateTime.now());
			batchCovJob.setUpdator(fileAgent.user);
			batchCovJob.setUpdated(ZonedDateTime.now());
			try {
				batchCovJobAgent.validateStart(batchCovJob);
				batchCovJobAgent.validateIfEodFileExists(batchCovJob);
				batchCovJob.setStatus(BatchCovJob.Status.started.name());
	    	    transactionFileStore.generate(batchCovJob);
			} catch (AppException e) {
				batchCovJob.setEnd(ZonedDateTime.now());
				batchCovJob.setStatus(BatchCovJob.Status.failed.name());
				batchCovJob.setMessages(e.getMessage());
			}
    	    batchCovJobStore.update(batchCovJob);
        });
    }
}
