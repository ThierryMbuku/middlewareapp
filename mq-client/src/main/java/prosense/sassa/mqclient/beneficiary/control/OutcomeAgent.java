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
import prosense.sassa.mqclient.beneficiary.entity.Outcome;


import javax.enterprise.context.Dependent;

import javax.inject.Inject;

import java.util.*;

import java.util.stream.Collectors;

import org.slf4j.Logger;


@Dependent
public class OutcomeAgent {
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

    public Outcome forCreate(ObjectNode objectNode) {
        final Outcome.OutcomeBuilder builder = Outcome.builder();
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
        Optional.ofNullable(objectNode.get(created)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::created);
        Optional.ofNullable(objectNode.get(creator)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::creator);
        Optional.ofNullable(objectNode.get(updated)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::updated);
        Optional.ofNullable(objectNode.get(updator)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::updator);
        return builder.build();
    }

    public Map<String, Object> toMap(final Outcome outcome) {
        return toFilteredMap(outcome, ImmutableSet.of(id, application, idNumber, socpenUser, office, socpenTime, method, ref, type1, type2, type3, type4, status1, status2, status3, status4, reason1, reason2, reason3, reason4, created, creator, updated, updator));
    }

    public Map<String, Object> toFilteredMap(final Outcome outcome, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(id, outcome.getId());
        fields.forEach(f -> {
            if (application.equals(f)) {
                map.put(application, outcome.getApplication());
            }
            if (idNumber.equals(f)) {
                map.put(idNumber, outcome.getIdNumber());
            }
            if (socpenUser.equals(f)) {
                map.put(socpenUser, outcome.getSocpenUser());
            }
            if (office.equals(f)) {
                map.put(office, outcome.getOffice());
            }
            if (socpenTime.equals(f)) {
                map.put(socpenTime, outcome.getSocpenTime());
            }
            if (method.equals(f)) {
                map.put(method, outcome.getMethod());
            }
            if (ref.equals(f)) {
                map.put(ref, outcome.getRef());
            }
            if (type1.equals(f)) {
                map.put(type1, outcome.getType1());
            }
            if (type2.equals(f)) {
                map.put(type2, outcome.getType2());
            }
            if (type3.equals(f)) {
                map.put(type3, outcome.getType3());
            }
            if (type4.equals(f)) {
                map.put(type4, outcome.getType4());
            }
            if (status1.equals(f)) {
                map.put(status1, outcome.getStatus1());
            }
            if (status2.equals(f)) {
                map.put(status2, outcome.getStatus2());
            }
            if (status3.equals(f)) {
                map.put(status3, outcome.getStatus3());
            }
            if (status4.equals(f)) {
                map.put(status4, outcome.getStatus4());
            }
            if (reason1.equals(f)) {
                map.put(reason1, outcome.getReason1());
            }
            if (reason2.equals(f)) {
                map.put(reason2, outcome.getReason2());
            }
            if (reason3.equals(f)) {
                map.put(reason3, outcome.getReason3());
            }
            if (reason4.equals(f)) {
                map.put(reason4, outcome.getReason4());
            }
            if (created.equals(f)) {
                map.put(created, outcome.getCreated());
            }
            if (creator.equals(f)) {
                map.put(creator, outcome.getCreator());
            }
            if (updated.equals(f)) {
                map.put(updated, outcome.getUpdated());
            }
            if (updator.equals(f)) {
                map.put(updator, outcome.getUpdator());
            }
        });
        return map;
    }
}
