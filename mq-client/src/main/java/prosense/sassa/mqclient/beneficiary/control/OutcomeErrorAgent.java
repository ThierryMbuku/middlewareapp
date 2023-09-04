package prosense.sassa.mqclient.beneficiary.control;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.slf4j.Logger;

import java.time.ZonedDateTime;

import prosense.sassa.mqclient.api.control.App;
import prosense.sassa.mqclient.api.entity.AppException;
import prosense.sassa.mqclient.beneficiary.entity.OutcomeError;


import javax.enterprise.context.Dependent;

import javax.inject.Inject;

import java.util.*;

import java.util.stream.Collectors;

import org.slf4j.Logger;


@Dependent
public class OutcomeErrorAgent {
    @Inject
    @App
    Logger logger;

    public static final String id = "id";
    public static final String application = "application";
    public static final String idNumber = "idNumber";
    public static final String socpenUser = "socpenUser";
    public static final String office = "office";
    public static final String socpenTime = "socpenTime";
    public static final String method = "method";
    public static final String ref = "ref";
    public static final String type1 = "type1";
    public static final String type2 = "type2";
    public static final String type3 = "type3";
    public static final String type4 = "type4";
    public static final String status1 = "status1";
    public static final String status2 = "status2";
    public static final String status3 = "status3";
    public static final String status4 = "status4";
    public static final String reason1 = "reason1";
    public static final String reason2 = "reason2";
    public static final String reason3 = "reason3";
    public static final String reason4 = "reason4";
    public static final String messageId = "messageId";
    public static final String errorCode = "errorCode";
    public static final String errorMessage = "errorMessage";
    public static final String rawMessage = "rawMessage";
    public static final String api = "api";
    public static final String messageStatus = "messageStatus";
    public static final String created = "created";
    public static final String creator = "creator";
    public static final String updated = "updated";
    public static final String updator = "updator";

    public void validate(final ObjectNode objectNode) {
        final Set<String> messages = Sets.newHashSet();
        if (!messages.isEmpty()) {
            throw AppException.builder()
                              .badRequest400()
                              .messages(messages)
                              .build();
        }
    }

    public OutcomeError forCreate(ObjectNode objectNode) {
        final OutcomeError.OutcomeErrorBuilder builder = OutcomeError.builder();
        Optional.ofNullable(objectNode.get(id)).filter(f -> !f.isNull()).map(JsonNode::asLong).ifPresent(builder::id);
        Optional.ofNullable(objectNode.get(application)).filter(f -> !f.isNull()).map(JsonNode::asLong).ifPresent(builder::application);
        Optional.ofNullable(objectNode.get(idNumber)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::idNumber);
        Optional.ofNullable(objectNode.get(socpenUser)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::socpenUser);
        Optional.ofNullable(objectNode.get(office)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::office);
        Optional.ofNullable(objectNode.get(socpenTime)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::socpenTime);
        Optional.ofNullable(objectNode.get(method)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::method);
        Optional.ofNullable(objectNode.get(ref)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::ref);
        Optional.ofNullable(objectNode.get(type1)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::type1);
        Optional.ofNullable(objectNode.get(type2)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::type2);
        Optional.ofNullable(objectNode.get(type3)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::type3);
        Optional.ofNullable(objectNode.get(type4)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::type4);
        Optional.ofNullable(objectNode.get(status1)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::status1);
        Optional.ofNullable(objectNode.get(status2)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::status2);
        Optional.ofNullable(objectNode.get(status3)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::status3);
        Optional.ofNullable(objectNode.get(status4)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::status4);
        Optional.ofNullable(objectNode.get(reason1)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::reason1);
        Optional.ofNullable(objectNode.get(reason2)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::reason2);
        Optional.ofNullable(objectNode.get(reason3)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::reason3);
        Optional.ofNullable(objectNode.get(reason4)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::reason4);
        Optional.ofNullable(objectNode.get(messageId)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::messageId);
        Optional.ofNullable(objectNode.get(errorCode)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::errorCode);
        Optional.ofNullable(objectNode.get(errorMessage)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::errorMessage);
        Optional.ofNullable(objectNode.get(rawMessage)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::rawMessage);
        Optional.ofNullable(objectNode.get(api)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::api);
        Optional.ofNullable(objectNode.get(messageStatus)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::messageStatus);
        builder.created(ZonedDateTime.now());
        Optional.ofNullable(objectNode.get(creator)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::creator);
        return builder.build();
    }

    public void validateCreate(final OutcomeError outcomeError) {
        final Set<String> messages = Sets.newHashSet();
        if (outcomeError.getId() != null) {
            messages.add(id + " invalid");
        }
        if (outcomeError.getCreator() == null) {
            messages.add(creator + " mandatory");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder()
                              .unprocessableEntity422()
                              .messages(messages)
                              .build();
        }
    }

    public OutcomeError forUpdate(final OutcomeError outcomeError, final ObjectNode objectNode) {
        Optional.ofNullable(objectNode.get(application)).ifPresent(n -> outcomeError.setApplication(n.isNull() ? null : n.asLong()));
        Optional.ofNullable(objectNode.get(idNumber)).ifPresent(n -> outcomeError.setIdNumber(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(socpenUser)).ifPresent(n -> outcomeError.setSocpenUser(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(office)).ifPresent(n -> outcomeError.setOffice(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(socpenTime)).ifPresent(n -> outcomeError.setSocpenTime(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(method)).ifPresent(n -> outcomeError.setMethod(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(ref)).ifPresent(n -> outcomeError.setRef(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(type1)).ifPresent(n -> outcomeError.setType1(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(type2)).ifPresent(n -> outcomeError.setType2(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(type3)).ifPresent(n -> outcomeError.setType3(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(type4)).ifPresent(n -> outcomeError.setType4(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(status1)).ifPresent(n -> outcomeError.setStatus1(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(status2)).ifPresent(n -> outcomeError.setStatus2(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(status3)).ifPresent(n -> outcomeError.setStatus3(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(status4)).ifPresent(n -> outcomeError.setStatus4(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(reason1)).ifPresent(n -> outcomeError.setReason1(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(reason2)).ifPresent(n -> outcomeError.setReason2(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(reason3)).ifPresent(n -> outcomeError.setReason3(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(reason4)).ifPresent(n -> outcomeError.setReason4(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(messageId)).ifPresent(n -> outcomeError.setMessageId(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(errorCode)).ifPresent(n -> outcomeError.setErrorCode(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(errorMessage)).ifPresent(n -> outcomeError.setErrorMessage(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(rawMessage)).ifPresent(n -> outcomeError.setRawMessage(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(api)).ifPresent(n -> outcomeError.setApi(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(messageStatus)).ifPresent(n -> outcomeError.setMessageStatus(n.isNull() ? null : n.asText()));
        Optional.ofNullable(objectNode.get(updator)).ifPresent(n -> outcomeError.setUpdator(n.isNull() ? null : n.asText()));
        outcomeError.setUpdated(ZonedDateTime.now());
        return outcomeError;
    }
        
    public void validateUpdate(final OutcomeError outcomeError) {
        final Set<String> messages = Sets.newHashSet();
        if (outcomeError.getId() == null) {
            messages.add(id + " invalid");
        }
        if (outcomeError.getUpdator() == null) {
            messages.add(updator + " mandatory");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder()
                              .unprocessableEntity422()
                              .messages(messages)
                              .build();
        }
    }

    public Map<String, Object> toMap(final OutcomeError outcomeError) {
        return toFilteredMap(outcomeError, ImmutableSet.of(id, application, idNumber, socpenUser, office, socpenTime, method, ref, type1, type2, type3, type4, status1, status2, status3, status4, reason1, reason2, reason3, reason4, messageId, errorCode, errorMessage, rawMessage, api, messageStatus, created, creator, updated, updator));
    }

    public Map<String, Object> toFilteredMap(final OutcomeError outcomeError, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(id, outcomeError.getId());
        fields.forEach(f -> {
            if (application.equals(f)) {
                map.put(application, outcomeError.getApplication());
            }
            if (idNumber.equals(f)) {
                map.put(idNumber, outcomeError.getIdNumber());
            }
            if (socpenUser.equals(f)) {
                map.put(socpenUser, outcomeError.getSocpenUser());
            }
            if (office.equals(f)) {
                map.put(office, outcomeError.getOffice());
            }
            if (socpenTime.equals(f)) {
                map.put(socpenTime, outcomeError.getSocpenTime());
            }
            if (method.equals(f)) {
                map.put(method, outcomeError.getMethod());
            }
            if (ref.equals(f)) {
                map.put(ref, outcomeError.getRef());
            }
            if (type1.equals(f)) {
                map.put(type1, outcomeError.getType1());
            }
            if (type2.equals(f)) {
                map.put(type2, outcomeError.getType2());
            }
            if (type3.equals(f)) {
                map.put(type3, outcomeError.getType3());
            }
            if (type4.equals(f)) {
                map.put(type4, outcomeError.getType4());
            }
            if (status1.equals(f)) {
                map.put(status1, outcomeError.getStatus1());
            }
            if (status2.equals(f)) {
                map.put(status2, outcomeError.getStatus2());
            }
            if (status3.equals(f)) {
                map.put(status3, outcomeError.getStatus3());
            }
            if (status4.equals(f)) {
                map.put(status4, outcomeError.getStatus4());
            }
            if (reason1.equals(f)) {
                map.put(reason1, outcomeError.getReason1());
            }
            if (reason2.equals(f)) {
                map.put(reason2, outcomeError.getReason2());
            }
            if (reason3.equals(f)) {
                map.put(reason3, outcomeError.getReason3());
            }
            if (reason4.equals(f)) {
                map.put(reason4, outcomeError.getReason4());
            }
            if (messageId.equals(f)) {
                map.put(messageId, outcomeError.getMessageId());
            }
            if (errorCode.equals(f)) {
                map.put(errorCode, outcomeError.getErrorCode());
            }
            if (errorMessage.equals(f)) {
                map.put(errorMessage, outcomeError.getErrorMessage());
            }
            if (rawMessage.equals(f)) {
                map.put(rawMessage, outcomeError.getRawMessage());
            }
            if (api.equals(f)) {
                map.put(api, outcomeError.getApi());
            }
            if (messageStatus.equals(f)) {
                map.put(messageStatus, outcomeError.getMessageStatus());
            }
            if (created.equals(f)) {
                map.put(created, outcomeError.getCreated());
            }
            if (creator.equals(f)) {
                map.put(creator, outcomeError.getCreator());
            }
            if (updated.equals(f)) {
                map.put(updated, outcomeError.getUpdated());
            }
            if (updator.equals(f)) {
                map.put(updator, outcomeError.getUpdator());
            }
        });
        return map;
    }
}
