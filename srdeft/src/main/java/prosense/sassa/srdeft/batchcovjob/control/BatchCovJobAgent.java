package prosense.sassa.srdeft.batchcovjob.control;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import prosense.sassa.srdeft.api.control.App;
import prosense.sassa.srdeft.api.control.Property;
import prosense.sassa.srdeft.api.entity.AppException;
import prosense.sassa.srdeft.batchcovjob.entity.BatchCovJob;
import prosense.sassa.srdeft.covjob.entity.CovJob;
import prosense.sassa.srdeft.covjob.control.CovJobAgent;
import prosense.sassa.srdeft.covjob.control.CovJobStore;
import prosense.sassa.srdeft.transmissionfile.entity.TransmissionFile;
import prosense.sassa.srdeft.transmissionfile.control.TransmissionFileAgent;
import prosense.sassa.srdeft.payday.entity.PayDay;
import prosense.sassa.srdeft.payday.control.PayDayAgent;

import javax.enterprise.context.Dependent;

import java.util.*;
import java.util.stream.Collectors;

import javax.inject.Inject;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.MultivaluedHashMap;


@Dependent
public class BatchCovJobAgent {
    @Inject
    @App
    Logger logger;

    @Inject
    @App
    DateTimeFormatter dateTimeFormatter;

    @Inject
    CovJobAgent covJobAgent;

    @Inject
    CovJobStore covJobStore;

    @Inject
    BatchCovJobStore batchCovJobStore;
    
    @Inject
    TransmissionFileAgent transmissionFileAgent;

    @Inject
    PayDayAgent payDayAgent;

    @Inject
    @Property
    private String filePath;
    
    public static final String id = "id";
    public static final String processingDate = "processingDate";
    public static final String verificationPeriod = "verificationPeriod";
    public static final String period = "period";
    public static final String payDay = "payDay";
    public static final String subService = "subService";
    public static final String type = "type";
    public static final String byRegion = "byRegion";
    public static final String test = "test";
    public static final String status = "status";
    public static final String signStatus = "signStatus";
    public static final String transactionsPerJob = "transactionsPerJob";
    public static final String transactionsTotal = "transactionsTotal";
    public static final String transactionsProcessed = "transactionsProcessed";
    public static final String transactionsRejected = "transactionsRejected";
    public static final String start = "start";
    public static final String end = "end";
    public static final String messages = "messages";
    public static final String covJobs = "covJobs";
    public static final String creator = "creator";
    public static final String created = "created";
    public static final String updator = "updator";
    public static final String updated = "updated";
    
    private boolean sameDay = false;
    private DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void validate(final ObjectNode objectNode) {
        final Set<String> messages = Sets.newHashSet();
        sameDay = false;
        Optional.ofNullable(objectNode.get(processingDate))
                .filter(f -> !f.isNull())
                .ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(processingDate + " invalid");
            } else {
                try {
                    sameDay = LocalDate.parse(n.asText(), dateFormatter).equals(LocalDate.now());
                } catch (DateTimeParseException e) {
                    messages.add(processingDate + " invalid: " + e.getMessage());
                }
            }
        });
        Optional.ofNullable(objectNode.get(verificationPeriod)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(verificationPeriod + " invalid");
            } else {
                try {
                    BatchCovJob.VerificationPeriod.valueOf(n.asText());
                } catch (IllegalArgumentException e) {
                    messages.add(verificationPeriod + " invalid: " + Arrays.toString(BatchCovJob.VerificationPeriod.values()));
                }
            }
        });
        Optional.ofNullable(objectNode.get(period)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(period + " invalid");
            } else {
                try {
                    BatchCovJob.Period.valueOf(n.asText());
                } catch (IllegalArgumentException e) {
                    messages.add(period + " invalid: " + Arrays.toString(BatchCovJob.Period.values()));
                }
            }
        });
        Optional.ofNullable(objectNode.get(payDay)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isNumber()) {
                messages.add(payDay + " invalid");
            }
        });
        Optional.ofNullable(objectNode.get(subService)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(subService + " invalid");
            } else {
                try {
                    BatchCovJob.SubService.valueOf(n.asText());
                } catch (IllegalArgumentException e) {
                    messages.add(subService + " invalid: " + Arrays.toString(BatchCovJob.SubService.values()));
                }
            }
        });
        Optional.ofNullable(objectNode.get(type)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(type + " invalid");
            } else {
                try {
                    BatchCovJob.Type.valueOf(n.asText());
                } catch (IllegalArgumentException e) {
                    messages.add(type + " invalid: " + Arrays.toString(BatchCovJob.Type.values()));
                }
            }
        });
        Optional.ofNullable(objectNode.get(byRegion)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isBoolean()) {
                messages.add(byRegion + " invalid");
            }
        });
        Optional.ofNullable(objectNode.get(test)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isBoolean()) {
                messages.add(test + " invalid");
            }
        });
        Optional.ofNullable(objectNode.get(signStatus)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(signStatus + " invalid");
            } else {
                try {
                    BatchCovJob.SignStatus.valueOf(n.asText());
                } catch (IllegalArgumentException e) {
                    messages.add(signStatus + " invalid: " + Arrays.toString(BatchCovJob.SignStatus.values()));
                }
            }
        });
        if (!messages.isEmpty()) {
            throw AppException.builder().badRequest400().messages(messages).build();
        }
    }

    public void validateCreate(final BatchCovJob batchCovJob) {
        final Set<String> messages = Sets.newHashSet();
        if (batchCovJob.getId() != null) {
            messages.add(id + " invalid");
        }
        if (batchCovJob.getProcessingDate() == null) {
            messages.add(processingDate + " mandatory");
        }
        if (batchCovJob.getVerificationPeriod() == null) {
            messages.add(verificationPeriod + " mandatory");
        }
        if (batchCovJob.getPeriod() == null) {
            messages.add(period + " mandatory");
        }
        if (batchCovJob.getPayDay() == null) {
            messages.add(payDay + " mandatory");
        }
        if (batchCovJob.getSubService() == null) {
            messages.add(subService + " mandatory");
        }
        if (batchCovJob.getType() == null) {
            messages.add(type + " mandatory");
        }
        if (batchCovJob.getByRegion() == null) {
            messages.add(byRegion + " mandatory");
        }
        if (batchCovJob.getTest() == null) {
            messages.add(test + " mandatory");
        }
        if (!sameDay && ZonedDateTime.now().compareTo(batchCovJob.getProcessingDate()) > 0) {
            messages.add(processingDate + " invalid");
        }
        if(sameDay) {
        	validateIfEodFileExists(batchCovJob);
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }
    
    public void validatePayDay(final BatchCovJob batchCovJob) {
        final Set<String> messages = Sets.newHashSet();
        PayDay payDay = payDayAgent.evaluate(PayDay.builder().processingDate(batchCovJob.getProcessingDate()).subService(batchCovJob.getSubService()).build());
        if (!batchCovJob.getPeriod().equals(payDay.getPeriod())) {
            messages.add(this.period + " invalid");
        }
        if (!batchCovJob.getPayDay().equals(payDay.getPayDay())) {
            messages.add(this.payDay + " invalid");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public void validateIfEodFileExists(final BatchCovJob batchCovJob) {    
        final Set<String> messages = Sets.newHashSet();
        if(transmissionFileAgent.exists(TransmissionFile.builder().service(batchCovJob.getType()).subService(batchCovJob.getSubService()).type(TransmissionFile.Type.ctl_end.name()).test(batchCovJob.getTest()).build())) {
        	messages.add(subService + " " + batchCovJob.getSubService() + " closed for date " + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(ZonedDateTime.now()));
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public void validateIfExists(final BatchCovJob batchCovJob, String status) {
        final Set<String> messages = Sets.newHashSet();
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.add(processingDate, batchCovJob.getProcessingDate().format(DateTimeFormatter.ISO_INSTANT));
        queryParameters.add(verificationPeriod, batchCovJob.getVerificationPeriod());
        queryParameters.add(payDay, String.valueOf(batchCovJob.getPayDay()));
        queryParameters.add(this.status, status);
        Set<BatchCovJob> batchCovJobs = batchCovJobStore.search(queryParameters);
        if (batchCovJobs.size() > 0) {
            messages.add("batchCovJob " + id + " " + batchCovJobs.stream().findFirst().get().getId() + " with status '" + status + "' already exists for given parameters");
        }
        queryParameters = new MultivaluedHashMap<>();
        queryParameters.add(verificationPeriod, batchCovJob.getVerificationPeriod());
        queryParameters.add(payDay, String.valueOf(batchCovJob.getPayDay()));
        queryParameters.add(this.status, status);
        Set<CovJob> covJobs = covJobStore.search(queryParameters);
        if (covJobs.size() > 0) {
            messages.add("covJob " + id + " " + covJobs.stream().findFirst().get().getId() + " with status '" + status + "' already exists for given parameters");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }
    
    public void validateIfApprovedOutcomeExists(final BatchCovJob batchCovJob) {
        if(sameDay)
        	covJobAgent.validateIfApprovedOutcomeExists(CovJob.builder().verificationPeriod(batchCovJob.getVerificationPeriod()).period(batchCovJob.getPeriod()).payDay(batchCovJob.getPayDay()).build());
    }
    
    public BatchCovJob forCreate(final ObjectNode objectNode, final String user) {
        final BatchCovJob.BatchCovJobBuilder builder = BatchCovJob.builder();
        Optional.ofNullable(objectNode.get(processingDate)).map(m -> LocalDate.parse(m.asText(), dateFormatter).atStartOfDay(ZoneId.systemDefault())).ifPresent(builder::processingDate);
        Optional.ofNullable(objectNode.get(verificationPeriod)).map(JsonNode::asText).ifPresent(builder::verificationPeriod);
        Optional.ofNullable(objectNode.get(period)).map(JsonNode::asText).ifPresent(builder::period);
        Optional.ofNullable(objectNode.get(payDay)).map(JsonNode::asLong).ifPresent(builder::payDay);
        Optional.ofNullable(objectNode.get(subService)).map(JsonNode::asText).ifPresent(builder::subService);
        Optional.ofNullable(objectNode.get(type)).map(JsonNode::asText).ifPresent(builder::type);
        Optional.ofNullable(objectNode.get(byRegion)).map(JsonNode::asBoolean).ifPresent(builder::byRegion);
        Optional.ofNullable(objectNode.get(test)).map(JsonNode::asBoolean).ifPresent(builder::test);
        builder.status(BatchCovJob.Status.pending.name());
        builder.creator(user);
        builder.created(ZonedDateTime.now());
        return builder.build();
    }

    public void validateSign(final BatchCovJob batchCovJob) {
        final Set<String> messages = Sets.newHashSet();
        if (!batchCovJob.getStatus().equals(BatchCovJob.Status.pending.name())) {
            messages.add(status + " invalid");
        }
        if (batchCovJob.getSignStatus() == null) {
            messages.add(signStatus + " mandatory");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public BatchCovJob forSign(final BatchCovJob batchCovJob, final ObjectNode objectNode, final String user) {
        Optional.ofNullable(objectNode.get(signStatus)).ifPresent(n -> batchCovJob.setSignStatus(n.isNull() ? null : n.asText()));
        batchCovJob.setUpdator(user);
        batchCovJob.setUpdated(ZonedDateTime.now());
        return batchCovJob;
    }

    public void validateStart(final BatchCovJob batchCovJob) {
        final Set<String> messages = Sets.newHashSet();
        if (!batchCovJob.getStatus().equals(BatchCovJob.Status.pending.name())) {
            messages.add(status + " invalid");
        }
        if (batchCovJob.getSignStatus() == null || !batchCovJob.getSignStatus().equals(BatchCovJob.SignStatus.approved.name())) {
            messages.add(signStatus + " invalid");
        }
        if (!Files.exists(Paths.get(filePath)) || !Files.isWritable(Paths.get(filePath))) {
            messages.add("file path " + filePath + " invalid");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public BatchCovJob forStart(final BatchCovJob batchCovJob, final ObjectNode objectNode, final String user) {
        batchCovJob.setStart(ZonedDateTime.now());
        batchCovJob.setUpdator(user);
        batchCovJob.setUpdated(ZonedDateTime.now());
        return batchCovJob;
    }

    public void validateCancel(final BatchCovJob batchCovJob) {
        final Set<String> messages = Sets.newHashSet();
        if (!batchCovJob.getStatus().equals(BatchCovJob.Status.pending.name())) {
            messages.add(status + " invalid");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public BatchCovJob forCancel(final BatchCovJob batchCovJob, final ObjectNode objectNode, final String user) {
        batchCovJob.setUpdator(user);
        batchCovJob.setUpdated(ZonedDateTime.now());
        return batchCovJob;
    }

    public Map<String, Object> toMap(final BatchCovJob batchCovJob) {
        return toFilteredMap(batchCovJob, ImmutableSet.of(processingDate, verificationPeriod, period, payDay, subService, type, byRegion, test, status, signStatus, transactionsPerJob, transactionsTotal, transactionsProcessed, transactionsRejected, start, end, messages, covJobs, creator, created, updator, updated));
    }

    public Map<String, Object> toFilteredMap(final BatchCovJob batchCovJob, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(id, batchCovJob.getId());
        fields.forEach(f -> {
            if (processingDate.equals(f)) {
                map.put(processingDate, dateFormatter.format(batchCovJob.getProcessingDate()));
            }
            if (verificationPeriod.equals(f)) {
                map.put(verificationPeriod, batchCovJob.getVerificationPeriod());
            }
            if (period.equals(f)) {
                map.put(period, batchCovJob.getPeriod());
            }
            if (payDay.equals(f)) {
                map.put(payDay, batchCovJob.getPayDay());
            }
            if (subService.equals(f)) {
                map.put(subService, batchCovJob.getSubService());
            }
            if (type.equals(f)) {
                map.put(type, batchCovJob.getType());
            }
            if (byRegion.equals(f)) {
                map.put(byRegion, batchCovJob.getByRegion());
            }
            if (test.equals(f)) {
                map.put(test, batchCovJob.getTest());
            }
            if (status.equals(f)) {
                map.put(status, batchCovJob.getStatus());
            }
            if (signStatus.equals(f)) {
                map.put(signStatus, batchCovJob.getSignStatus());
            }
            if (transactionsPerJob.equals(f)) {
                map.put(transactionsPerJob, batchCovJob.getTransactionsPerJob());
            }
            if (transactionsTotal.equals(f)) {
                map.put(transactionsTotal, batchCovJob.getTransactionsTotal());
            }
            if (transactionsProcessed.equals(f)) {
                map.put(transactionsProcessed, batchCovJob.getTransactionsProcessed());
            }
            if (transactionsRejected.equals(f)) {
                map.put(transactionsRejected, batchCovJob.getTransactionsRejected());
            }
            if (start.equals(f)) {
                map.put(start, batchCovJob.getStart());
            }
            if (end.equals(f)) {
                map.put(end, batchCovJob.getEnd());
            }
            if (messages.equals(f)) {
                map.put(messages, batchCovJob.getMessages());
            }
            if (covJobs.equals(f) && batchCovJob.getCovJobs() != null) {
                map.put(covJobs, batchCovJob.getCovJobs().stream()
                                   .map(covJob -> covJobAgent.toMap(covJob))
                                   .collect(Collectors.toCollection(LinkedHashSet::new)));
            }
            if (creator.equals(f)) {
                map.put(creator, batchCovJob.getCreator());
            }
            if (created.equals(f)) {
                map.put(created, batchCovJob.getCreated());
            }
            if (updator.equals(f)) {
                map.put(updator, batchCovJob.getUpdator());
            }
            if (updated.equals(f)) {
                map.put(updated, batchCovJob.getUpdated());
            }
        });
        return map;
    }
}
