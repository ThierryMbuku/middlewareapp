package prosense.sassa.srdeft.publicholiday.control;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.time.LocalTime;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import prosense.sassa.srdeft.api.control.App;
import prosense.sassa.srdeft.api.entity.AppException;
import prosense.sassa.srdeft.publicholiday.entity.PublicHoliday;
import prosense.sassa.srdeft.covjob.entity.CovJob;
import prosense.sassa.srdeft.covjob.control.CovJobStore;
import prosense.sassa.srdeft.batchcovjob.entity.BatchCovJob;
import prosense.sassa.srdeft.batchcovjob.control.BatchCovJobStore;

import javax.enterprise.context.Dependent;

import javax.inject.Inject;

import java.util.*;
import java.util.stream.Collectors;

import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.MultivaluedHashMap;


@Dependent
public class PublicHolidayAgent {
    
    @Inject
    PublicHolidayStore publicHolidayStore;

    @Inject
    CovJobStore covJobStore;

    @Inject
    BatchCovJobStore batchCovJobStore;
    
    @Inject
    @App
    DateTimeFormatter dateTimeFormatter;

    public static final String id = "id";
    public static final String date = "date";
    public static final String name = "name";
    public static final String description = "description";
    public static final String creator = "creator";
    public static final String created = "created";
    public static final String updator = "updator";
    public static final String updated = "updated";

    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public void validate(final ObjectNode objectNode) {
        final Set<String> messages = Sets.newHashSet();
        Optional.ofNullable(objectNode.get(date)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(date + " invalid");
            } else {
                try {
                    LocalDate.parse(n.asText(), dateFormatter);
                } catch (DateTimeParseException e) {
                    messages.add(date + " invalid: " + e.getMessage());
                }
            }
        });
        Optional.ofNullable(objectNode.get(name)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(name + " invalid");
            } else {
                if (n.asText().length() > 50) {
                    messages.add(name + " max length 50");
                }
            }
        });
        Optional.ofNullable(objectNode.get(description)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(description + " invalid");
            } else {
                if (n.asText().length() > 100) {
                    messages.add(description + " max length 100");
                }
            }
        });
        if (!messages.isEmpty()) {
            throw AppException.builder().badRequest400().messages(messages).build();
        }
    }

    public void validateCreate(final PublicHoliday publicHoliday) {
        final Set<String> messages = Sets.newHashSet();
        if (publicHoliday.getId() != null) {
            messages.add(id + " invalid");
        }
        if (publicHoliday.getDate() == null) {
            messages.add(date + " mandatory");
        }
        if (publicHoliday.getName() == null) {
            messages.add(name + " mandatory");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public PublicHoliday forCreate(final ObjectNode objectNode, final String user) {
        final PublicHoliday.PublicHolidayBuilder builder = PublicHoliday.builder();
        Optional.ofNullable(objectNode.get(date)).map(m -> LocalDate.parse(m.asText(), dateFormatter).atStartOfDay(ZoneId.systemDefault())).ifPresent(builder::date);
        Optional.ofNullable(objectNode.get(name)).map(JsonNode::asText).ifPresent(builder::name);
        Optional.ofNullable(objectNode.get(description)).map(JsonNode::asText).ifPresent(builder::description);
        builder.creator(user);
        builder.created(ZonedDateTime.now());
        return builder.build();
    }

    public void validateUpdate(final PublicHoliday publicHoliday) {
        final Set<String> messages = Sets.newHashSet();
        if (publicHoliday.getDate() == null) {
            messages.add(date + " mandatory");
        }
        if (publicHoliday.getName() == null) {
            messages.add(name + " mandatory");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public PublicHoliday forUpdate(final PublicHoliday publicHoliday, final ObjectNode objectNode, final String user) {
        Optional.ofNullable(objectNode.get(name)).ifPresent(n -> publicHoliday.setName(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(description)).ifPresent(n -> publicHoliday.setDescription(n.isNull() ? null : n.asText()));
        publicHoliday.setUpdator(user);
        publicHoliday.setUpdated(ZonedDateTime.now());
        return publicHoliday;
    }

    public void validateIfDateExists(final PublicHoliday publicHoliday) {
        final Set<String> messages = Sets.newHashSet();
        final MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.add(date, dateFormatter.format(publicHoliday.getDate()));
        Set<PublicHoliday> publicHolidays = publicHolidayStore.search(queryParameters);
        if (publicHolidays.size() > 0) {
            messages.add("publicHoliday " + id + " " + publicHolidays.stream().findFirst().get().getId() + " with date '" + publicHoliday.getDate().format(dateFormatter) + "' already exists");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }
    
    public void validateIfCovJobsExists(final PublicHoliday publicHoliday) {
    	checkCovJobs(CovJob.Status.pending.name());
    	checkCovJobs(CovJob.Status.started.name());
    	checkBatchCovJobs(publicHoliday.getDate(), BatchCovJob.Status.pending.name());
    	checkBatchCovJobs(publicHoliday.getDate(), BatchCovJob.Status.started.name());
    }
    
    private void checkCovJobs(String status) {
        final Set<String> messages = Sets.newHashSet();
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.add("status", status);
        queryParameters.add("from", ZonedDateTime.now().with(LocalTime.MIN).format(DateTimeFormatter.ISO_INSTANT));
        queryParameters.add("to", ZonedDateTime.now().with(LocalTime.MAX).format(DateTimeFormatter.ISO_INSTANT));
        Set<CovJob> covJobs = covJobStore.search(queryParameters);
        if (covJobs.size() > 0) {
            messages.add("covJob " + id + " " + covJobs.stream().findFirst().get().getId() + " with status '" + status + "' exists");
        }
        queryParameters = new MultivaluedHashMap<>();
        queryParameters.add("status", status);
        queryParameters.add("period", DateTimeFormatter.ofPattern("MMMyyyy").format(ZonedDateTime.now()));
        queryParameters.add("payDay", DateTimeFormatter.ofPattern("dd").format(ZonedDateTime.now()));
        covJobs = covJobStore.search(queryParameters);
        if (covJobs.size() > 0) {
            messages.add("covJob " + id + " " + covJobs.stream().findFirst().get().getId() + " with status '" + status + "' exists");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }
    
    private void checkBatchCovJobs(ZonedDateTime processingDate, String status) {
        final Set<String> messages = Sets.newHashSet();
        MultivaluedMap<String, String> queryParameters = new MultivaluedHashMap<>();
        queryParameters.add("processingDate", processingDate.format(DateTimeFormatter.ISO_INSTANT));
        queryParameters.add("status", status);
        Set<BatchCovJob> batchCovJobs = batchCovJobStore.search(queryParameters);
        if (batchCovJobs.size() > 0) {
            messages.add("batchCovJob " + id + " " + batchCovJobs.stream().findFirst().get().getId() + " with status '" + status + "' exists");
        }
        queryParameters = new MultivaluedHashMap<>();
        queryParameters.add("status", status);
        queryParameters.add("period", DateTimeFormatter.ofPattern("MMMyyyy").format(processingDate));
        queryParameters.add("payDay", DateTimeFormatter.ofPattern("dd").format(processingDate));
        batchCovJobs = batchCovJobStore.search(queryParameters);
        if (batchCovJobs.size() > 0) {
            messages.add("batchCovJob " + id + " " + batchCovJobs.stream().findFirst().get().getId() + " with status '" + status + "' exists");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }
    
    public Map<String, Object> toMap(final PublicHoliday publicHoliday) {
        return toFilteredMap(publicHoliday, ImmutableSet.of(date, name, description, creator, created, updator, updated));
    }

    public Map<String, Object> toFilteredMap(final PublicHoliday publicHoliday, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(id, publicHoliday.getId());
        fields.forEach(f -> {
            if (date.equals(f)) {
                map.put(date, dateFormatter.format(publicHoliday.getDate()));
            }
            if (name.equals(f)) {
                map.put(name, publicHoliday.getName());
            }
            if (description.equals(f)) {
                map.put(description, publicHoliday.getDescription());
            }
            if (creator.equals(f)) {
                map.put(creator, publicHoliday.getCreator());
            }
            if (created.equals(f)) {
                map.put(created, publicHoliday.getCreated());
            }
            if (updator.equals(f)) {
                map.put(updator, publicHoliday.getUpdator());
            }
            if (updated.equals(f)) {
                map.put(updated, publicHoliday.getUpdated());
            }
        });
        return map;
    }
}
