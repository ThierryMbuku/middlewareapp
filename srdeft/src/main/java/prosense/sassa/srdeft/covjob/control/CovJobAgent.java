package prosense.sassa.srdeft.covjob.control;

import org.slf4j.Logger;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import org.apache.commons.lang3.StringUtils;

import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import prosense.sassa.srdeft.api.control.App;
import prosense.sassa.srdeft.api.control.Property;
import prosense.sassa.srdeft.api.entity.AppException;
import prosense.sassa.srdeft.covjob.entity.CovJob;
import prosense.sassa.srdeft.cashbook.control.CashBookAgent;
import prosense.sassa.srdeft.transmissionfile.entity.TransmissionFile;
import prosense.sassa.srdeft.transmissionfile.control.TransmissionFileAgent;
import prosense.sassa.srdeft.outcome.srd.boundary.OutcomesResource;
import prosense.sassa.srdeft.payday.entity.PayDay;
import prosense.sassa.srdeft.payday.control.PayDayAgent;
import prosense.sassa.srdeft.batchcovjob.entity.BatchCovJob;
import prosense.sassa.srdeft.batchcovjob.control.BatchCovJobStore;

import javax.enterprise.context.Dependent;

import java.util.*;
import java.util.stream.Collectors;

import javax.inject.Inject;

import java.nio.file.Files;
import java.nio.file.Paths;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.MultivaluedHashMap;


@Dependent
public class CovJobAgent {
    @Inject
    @App
    Logger logger;

    @Inject
    CashBookAgent cashBookAgent;

    @Inject
    CovJobStore covJobStore;

    @Inject
    TransmissionFileAgent transmissionFileAgent;

    @Inject
    OutcomesResource outcomesResource;

    @Inject
    PayDayAgent payDayAgent;

    @Inject
    @Property
    private String filePath;
    
    @Inject
    BatchCovJobStore batchCovJobStore;
    
    public static final String id = "id";
    public static final String verificationPeriod = "verificationPeriod";
    public static final String period = "period";
    public static final String payDay = "payDay";
    public static final String subService = "subService";
    public static final String type = "type";
    public static final String byRegion = "byRegion";
    public static final String test = "test";
    public static final String status = "status";
    public static final String signStatus = "signStatus";
    public static final String batchCovJob = "batchCovJob";
    public static final String start = "start";
    public static final String end = "end";
    public static final String messages = "messages";
    public static final String cashBooks = "cashBooks";
    public static final String creator = "creator";
    public static final String created = "created";
    public static final String updator = "updator";
    public static final String updated = "updated";

    public void validate(final ObjectNode objectNode) {
        final Set<String> messages = Sets.newHashSet();
        Optional.ofNullable(objectNode.get(verificationPeriod)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(verificationPeriod + " invalid");
            } else {
                try {
                    CovJob.VerificationPeriod.valueOf(n.asText());
                } catch (IllegalArgumentException e) {
                    messages.add(verificationPeriod + " invalid: " + Arrays.toString(CovJob.VerificationPeriod.values()));
                }
            }
        });
        Optional.ofNullable(objectNode.get(period)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(period + " invalid");
            } else {
                try {
                    CovJob.Period.valueOf(n.asText());
                } catch (IllegalArgumentException e) {
                    messages.add(period + " invalid: " + Arrays.toString(CovJob.Period.values()));
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
                    CovJob.SubService.valueOf(n.asText());
                } catch (IllegalArgumentException e) {
                    messages.add(subService + " invalid: " + Arrays.toString(CovJob.SubService.values()));
                }
            }
        });
        Optional.ofNullable(objectNode.get(type)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(type + " invalid");
            } else {
                try {
                    CovJob.Type.valueOf(n.asText());
                } catch (IllegalArgumentException e) {
                    messages.add(type + " invalid: " + Arrays.toString(CovJob.Type.values()));
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
                    CovJob.SignStatus.valueOf(n.asText());
                } catch (IllegalArgumentException e) {
                    messages.add(signStatus + " invalid: " + Arrays.toString(CovJob.SignStatus.values()));
                }
            }
        });
        if (!messages.isEmpty()) {
            throw AppException.builder().badRequest400().messages(messages).build();
        }
    }

    public void validateCreate(final CovJob covJob) {
        final Set<String> messages = Sets.newHashSet();
        if (covJob.getId() != null) {
            messages.add(id + " invalid");
        }
        if (covJob.getVerificationPeriod() == null) {
            messages.add(verificationPeriod + " mandatory");
        }
        if (covJob.getPeriod() == null) {
            messages.add(period + " mandatory");
        }
        if (covJob.getPayDay() == null) {
            messages.add(payDay + " mandatory");
        }
        if (covJob.getSubService() == null) {
            messages.add(subService + " mandatory");
        }
        if (covJob.getType() == null) {
            messages.add(type + " mandatory");
        }
        if (covJob.getByRegion() == null) {
            messages.add(byRegion + " mandatory");
        }
        if (covJob.getTest() == null) {
            messages.add(test + " mandatory");
        }
        validateIfEodFileExists(covJob);
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public void validatePayDay(final CovJob covJob) {
        final Set<String> messages = Sets.newHashSet();
        PayDay payDay = payDayAgent.evaluate(PayDay.builder().processingDate(covJob.getCreated()).subService(covJob.getSubService()).build());
        if (!covJob.getPeriod().equals(payDay.getPeriod())) {
            messages.add(this.period + " invalid");
        }
        if (!covJob.getPayDay().equals(payDay.getPayDay())) {
            messages.add(this.payDay + " invalid");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public void validateIfEodFileExists(final CovJob covJob) {    
        final Set<String> messages = Sets.newHashSet();
        if(transmissionFileAgent.exists(TransmissionFile.builder().service(covJob.getType()).subService(covJob.getSubService()).type(TransmissionFile.Type.ctl_end.name()).test(covJob.getTest()).build())) {
        	messages.add(subService + " " + covJob.getSubService() + " closed for date " + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(ZonedDateTime.now()));
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public void validateIfExists(final CovJob covJob, String status) {
        final Set<String> messages = Sets.newHashSet();
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.add(verificationPeriod, covJob.getVerificationPeriod());
        queryParameters.add(payDay, String.valueOf(covJob.getPayDay()));
        queryParameters.add(this.status, status);
        Set<CovJob> covJobs = covJobStore.search(queryParameters);
        if (covJobs.size() > 0) {
            messages.add("covJob " + id + " " + covJobs.stream().findFirst().get().getId() + " with status '" + status + "' already exists for given parameters");
        }
        queryParameters = new MultivaluedHashMap<>();
        queryParameters.add("processingDate", ZonedDateTime.now().with(LocalTime.MIN).format(DateTimeFormatter.ISO_INSTANT));
        queryParameters.add("status", status);
        Set<BatchCovJob> batchCovJobs = batchCovJobStore.search(queryParameters);
        if (batchCovJobs.size() > 0) {
            messages.add("batchCovJob " + id + " " + batchCovJobs.stream().findFirst().get().getId() + " with status '" + status + "' exists");
        }
        queryParameters = new MultivaluedHashMap<>();
        queryParameters.add("status", status);
        queryParameters.add("period", covJob.getPeriod());
        queryParameters.add("payDay", covJob.getPayDay().toString());
        batchCovJobs = batchCovJobStore.search(queryParameters);
        if (batchCovJobs.size() > 0) {
            messages.add("batchCovJob " + id + " " + batchCovJobs.stream().findFirst().get().getId() + " with status '" + status + "' exists");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }
    
    public void validateIfApprovedOutcomeExists(final CovJob covJob) {
        final Set<String> messages = Sets.newHashSet();
        try {
			if(outcomesResource.searchApprovedByCovJob(covJob, 1).size() == 0) {
				messages.add("no approved outcome exists for given parameters");
			}
		} catch (Exception e) {
			logger.error("exception", e);
			messages.add(StringUtils.substring(e.getMessage(), 0, 200));
		}
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }
    
    public CovJob forCreate(final ObjectNode objectNode, final String user) {
        final CovJob.CovJobBuilder builder = CovJob.builder();
        Optional.ofNullable(objectNode.get(verificationPeriod)).map(JsonNode::asText).ifPresent(builder::verificationPeriod);
        Optional.ofNullable(objectNode.get(period)).map(JsonNode::asText).ifPresent(builder::period);
        Optional.ofNullable(objectNode.get(payDay)).map(JsonNode::asLong).ifPresent(builder::payDay);
        Optional.ofNullable(objectNode.get(subService)).map(JsonNode::asText).ifPresent(builder::subService);
        Optional.ofNullable(objectNode.get(type)).map(JsonNode::asText).ifPresent(builder::type);
        Optional.ofNullable(objectNode.get(byRegion)).map(JsonNode::asBoolean).ifPresent(builder::byRegion);
        Optional.ofNullable(objectNode.get(test)).map(JsonNode::asBoolean).ifPresent(builder::test);
        builder.status(CovJob.Status.pending.name());
        builder.creator(user);
        builder.created(ZonedDateTime.now());
        return builder.build();
    }

    public void validateSign(final CovJob covJob) {
        final Set<String> messages = Sets.newHashSet();
        if (!covJob.getStatus().equals(CovJob.Status.pending.name())) {
            messages.add(status + " invalid");
        }
        if (covJob.getSignStatus() == null) {
            messages.add(signStatus + " mandatory");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public CovJob forSign(final CovJob covJob, final ObjectNode objectNode, final String user) {
        Optional.ofNullable(objectNode.get(signStatus)).ifPresent(n -> covJob.setSignStatus(n.isNull() ? null : n.asText()));
        covJob.setUpdator(user);
        covJob.setUpdated(ZonedDateTime.now());
        return covJob;
    }

    public void validateStart(final CovJob covJob) {
        final Set<String> messages = Sets.newHashSet();
        if (!covJob.getStatus().equals(CovJob.Status.pending.name())) {
            messages.add(status + " invalid");
        }
        if (covJob.getSignStatus() == null || !covJob.getSignStatus().equals(CovJob.SignStatus.approved.name())) {
            messages.add(signStatus + " invalid");
        }
        if (!Files.exists(Paths.get(filePath)) || !Files.isWritable(Paths.get(filePath))) {
            messages.add("file path " + filePath + " invalid");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public CovJob forStart(final CovJob covJob, final ObjectNode objectNode, final String user) {
        covJob.setStart(ZonedDateTime.now());
        covJob.setUpdator(user);
        covJob.setUpdated(ZonedDateTime.now());
        return covJob;
    }

    public Map<String, Object> toMap(final CovJob covJob) {
        return toFilteredMap(covJob, ImmutableSet.of(verificationPeriod, period, payDay, subService, type, byRegion, test, status, signStatus, batchCovJob, start, end, messages, cashBooks, creator, created, updator, updated));
    }

    public Map<String, Object> toFilteredMap(final CovJob covJob, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(id, covJob.getId());
        fields.forEach(f -> {
            if (verificationPeriod.equals(f)) {
                map.put(verificationPeriod, covJob.getVerificationPeriod());
            }
            if (period.equals(f)) {
                map.put(period, covJob.getPeriod());
            }
            if (payDay.equals(f)) {
                map.put(payDay, covJob.getPayDay());
            }
            if (subService.equals(f)) {
                map.put(subService, covJob.getSubService());
            }
            if (type.equals(f)) {
                map.put(type, covJob.getType());
            }
            if (byRegion.equals(f)) {
                map.put(byRegion, covJob.getByRegion());
            }
            if (test.equals(f)) {
                map.put(test, covJob.getTest());
            }
            if (status.equals(f)) {
                map.put(status, covJob.getStatus());
            }
            if (signStatus.equals(f)) {
                map.put(signStatus, covJob.getSignStatus());
            }
            if (batchCovJob.equals(f)) {
                map.put(batchCovJob, covJob.getBatchCovJob());
            }
            if (start.equals(f)) {
                map.put(start, covJob.getStart());
            }
            if (end.equals(f)) {
                map.put(end, covJob.getEnd());
            }
            if (messages.equals(f)) {
                map.put(messages, covJob.getMessages());
            }
            if (cashBooks.equals(f) && covJob.getCashBooks() != null) {
                map.put(cashBooks, covJob.getCashBooks().stream()
                                   .map(cashBook -> cashBookAgent.toMap(cashBook))
                                   .collect(Collectors.toCollection(LinkedHashSet::new)));
            }
            if (creator.equals(f)) {
                map.put(creator, covJob.getCreator());
            }
            if (created.equals(f)) {
                map.put(created, covJob.getCreated());
            }
            if (updator.equals(f)) {
                map.put(updator, covJob.getUpdator());
            }
            if (updated.equals(f)) {
                map.put(updated, covJob.getUpdated());
            }
        });
        return map;
    }
}
