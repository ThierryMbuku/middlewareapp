package prosense.sassa.srdclient.mqevent.control;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import java.time.ZonedDateTime;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import prosense.sassa.srdclient.api.control.App;
import prosense.sassa.srdclient.api.entity.AppException;
import prosense.sassa.srdclient.mqevent.entity.MQEvent;

import javax.enterprise.context.Dependent;

import javax.inject.Inject;

import java.util.*;


@Dependent
public class MQEventAgent {
    @Inject
    @App
    DateTimeFormatter dateTimeFormatter;

    public static final String id = "id";
    public static final String outcomeId = "outcomeId";
    public static final String sequence = "sequence";
    public static final String messageId = "messageId";
    public static final String correlation = "correlation";
    public static final String type = "type";
    public static final String effectedBy = "effectedBy";
    public static final String effectedOn = "effectedOn";
    public static final String occurred = "occurred";
    public static final String context = "context";
    public static final String creator = "creator";
    public static final String created = "created";

    public void validate(final ObjectNode objectNode) {
        final Set<String> messages = Sets.newHashSet();
        Optional.ofNullable(objectNode.get(outcomeId))
                .filter(f -> !f.isNull())
                .ifPresent(n -> {
            if (!n.isNumber()) {
                messages.add(outcomeId + " invalid");
            }
        });
        Optional.ofNullable(objectNode.get(sequence))
                .filter(f -> !f.isNull())
                .ifPresent(n -> {
            if (!n.isNumber()) {
                messages.add(sequence + " invalid");
            }
        });
        Optional.ofNullable(objectNode.get(messageId))
                .filter(f -> !f.isNull())
                .ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(messageId + " invalid");
            } else {
                if (n.asText().length() > 60) {
                    messages.add(messageId + " max length 60");
                }
            }
        });
        Optional.ofNullable(objectNode.get(correlation))
                .filter(f -> !f.isNull())
                .ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(correlation + " invalid");
            } else {
                if (n.asText().length() > 60) {
                    messages.add(correlation + " max length 60");
                }
            }
        });
        Optional.ofNullable(objectNode.get(type))
                .filter(f -> !f.isNull())
                .ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(type + " invalid");
            } else {
                try {
                    MQEvent.MQEventType.valueOf(n.asText());
                } catch (IllegalArgumentException e) {
                    messages.add(type + " invalid: " + Arrays.toString(MQEvent.MQEventType.values()));
                }
            }
        });
        Optional.ofNullable(objectNode.get(effectedBy))
                .filter(f -> !f.isNull())
                .ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(effectedBy + " invalid");
            } else {
                if (n.asText().length() > 20) {
                    messages.add(effectedBy + " max length 20");
                }
            }
        });
        Optional.ofNullable(objectNode.get(effectedOn))
                .filter(f -> !f.isNull())
                .ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(effectedOn + " invalid");
            } else {
                if (n.asText().length() > 50) {
                    messages.add(effectedOn + " max length 50");
                }
            }
        });
        Optional.ofNullable(objectNode.get(occurred))
                .filter(f -> !f.isNull())
                .ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(occurred + " invalid");
            } else {
                try {
                    ZonedDateTime.of(LocalDateTime.parse(n.asText(), dateTimeFormatter), ZoneId.systemDefault());
                } catch (DateTimeParseException e) {
                    messages.add(occurred + " invalid: " + e.getMessage());
                }
            }
        });
        Optional.ofNullable(objectNode.get(context))
                .filter(f -> !f.isNull())
                .ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(context + " invalid");
            } else {
                if (n.asText().length() > 20) {
                    messages.add(context + " max length 20");
                }
            }
        });
        Optional.ofNullable(objectNode.get(creator))
                .filter(f -> !f.isNull())
                .ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(creator + " invalid");
            } else {
                if (n.asText().length() > 50) {
                    messages.add(creator + " max length 50");
                }
            }
        });
        if (!messages.isEmpty()) {
            throw AppException.builder()
                              .badRequest400()
                              .messages(messages)
                              .build();
        }
    }

    public MQEvent forCreate(ObjectNode objectNode) {
        final MQEvent.MQEventBuilder builder = MQEvent.builder();
        Optional.ofNullable(objectNode.get(outcomeId)).map(JsonNode::asLong).ifPresent(builder::outcomeId);
        Optional.ofNullable(objectNode.get(sequence)).map(JsonNode::asLong).ifPresent(builder::sequence);
        Optional.ofNullable(objectNode.get(messageId)).map(JsonNode::asText).ifPresent(builder::messageId);
        Optional.ofNullable(objectNode.get(correlation)).map(JsonNode::asText).ifPresent(builder::correlation);
        Optional.ofNullable(objectNode.get(type)).map(JsonNode::asText).ifPresent(builder::type);
        Optional.ofNullable(objectNode.get(effectedBy)).map(JsonNode::asText).ifPresent(builder::effectedBy);
        Optional.ofNullable(objectNode.get(effectedOn)).map(JsonNode::asText).ifPresent(builder::effectedOn);
        Optional.ofNullable(objectNode.get(occurred)).map(m -> ZonedDateTime.of(LocalDateTime.parse(m.asText(), dateTimeFormatter), ZoneId.systemDefault())).ifPresent(builder::occurred);
        Optional.ofNullable(objectNode.get(context)).map(JsonNode::asText).ifPresent(builder::context);
        Optional.ofNullable(objectNode.get(creator)).map(JsonNode::asText).ifPresent(builder::creator);
        builder.created(ZonedDateTime.now());
        return builder.build();
    }

    public void validateCreate(final MQEvent mqevent) {
        final Set<String> messages = Sets.newHashSet();
        if (mqevent.getId() != null) {
            messages.add(id + " invalid");
        }
        if (mqevent.getType() != null) {
// 	        if (mqevent.getMessageId() == null && (mqevent.getType().equals("PUT") || mqevent.getType().equals("GET"))) {
//     	        messages.add(messageId + " mandatory for type PUT or GET");
	        if (mqevent.getMessageId() == null && mqevent.getType().equals("GET")) {
    	        messages.add(messageId + " mandatory for type GET");
        	}
        }
        if (mqevent.getCreator() == null) {
            messages.add(creator + " mandatory");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder()
                              .unprocessableEntity422()
                              .messages(messages)
                              .build();
        }
    }

    public Map<String, Object> toMap(final MQEvent mqevent) {
        return toFilteredMap(mqevent, ImmutableSet.of(outcomeId, sequence, messageId, correlation, type, effectedBy, effectedOn, occurred, context, creator, created));
    }

    public Map<String, Object> toFilteredMap(final MQEvent mqevent, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(id, mqevent.getId());
        fields.forEach(f -> {
            if (outcomeId.equals(f)) {
                map.put(outcomeId, mqevent.getOutcomeId());
            }
            if (sequence.equals(f)) {
                map.put(sequence, mqevent.getSequence());
            }
            if (messageId.equals(f)) {
                map.put(messageId, mqevent.getMessageId());
            }
            if (correlation.equals(f)) {
                map.put(correlation, mqevent.getCorrelation());
            }
            if (type.equals(f)) {
                map.put(type, mqevent.getType());
            }
            if (effectedBy.equals(f)) {
                map.put(effectedBy, mqevent.getEffectedBy());
            }
            if (effectedOn.equals(f)) {
                map.put(effectedOn, mqevent.getEffectedOn());
            }
            if (occurred.equals(f)) {
                map.put(occurred, mqevent.getOccurred());
            }
            if (context.equals(f)) {
                map.put(context, mqevent.getContext());
            }
            if (creator.equals(f)) {
                map.put(creator, mqevent.getCreator());
            }
            if (created.equals(f)) {
                map.put(created, mqevent.getCreated());
            }
        });
        return map;
    }
}
