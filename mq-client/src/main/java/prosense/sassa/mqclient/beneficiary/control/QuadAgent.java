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
import prosense.sassa.mqclient.beneficiary.entity.Quad;


import javax.enterprise.context.Dependent;

import javax.inject.Inject;

import java.util.*;

import java.util.stream.Collectors;

import org.slf4j.Logger;


@Dependent
public class QuadAgent {
    @Inject
    @App
    Logger logger;

    public static final String quadNumber = "quadNumber";
    public static final String fullname = "fullname";
    public static final String surname = "surname";
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

    public Quad forCreate(ObjectNode objectNode) {
        final Quad.QuadBuilder builder = Quad.builder();
        Optional.ofNullable(objectNode.get(quadNumber)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::quadNumber);
        Optional.ofNullable(objectNode.get(fullname)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::fullname);
        Optional.ofNullable(objectNode.get(surname)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::surname);
        Optional.ofNullable(objectNode.get(created)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::created);
        Optional.ofNullable(objectNode.get(creator)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::creator);
        Optional.ofNullable(objectNode.get(updated)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::updated);
        Optional.ofNullable(objectNode.get(updator)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::updator);
        return builder.build();
    }

    public Map<String, Object> toMap(final Quad quad) {
        return toFilteredMap(quad, ImmutableSet.of(quadNumber, fullname, surname, created, creator, updated, updator));
    }

    public Map<String, Object> toFilteredMap(final Quad quad, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        fields.forEach(f -> {
            if (quadNumber.equals(f)) {
                map.put(quadNumber, quad.getQuadNumber());
            }
            if (fullname.equals(f)) {
                map.put(fullname, quad.getFullname());
            }
            if (surname.equals(f)) {
                map.put(surname, quad.getSurname());
            }
            if (created.equals(f)) {
                map.put(created, quad.getCreated());
            }
            if (creator.equals(f)) {
                map.put(creator, quad.getCreator());
            }
            if (updated.equals(f)) {
                map.put(updated, quad.getUpdated());
            }
            if (updator.equals(f)) {
                map.put(updator, quad.getUpdator());
            }
        });
        return map;
    }
}
