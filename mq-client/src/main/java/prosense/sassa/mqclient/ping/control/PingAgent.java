package prosense.sassa.mqclient.ping.control;

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
import prosense.sassa.mqclient.ping.entity.Ping;


import javax.enterprise.context.Dependent;

import javax.inject.Inject;

import java.util.*;

import java.util.stream.Collectors;

import org.slf4j.Logger;


@Dependent
public class PingAgent {
    @Inject
    @App
    Logger logger;

    public static final String iamPUT = "iamPUT";
    public static final String correlation = "correlation";
    public static final String socpenPUT = "socpenPUT";

    public void validate(final ObjectNode objectNode) {
        final Set<String> messages = Sets.newHashSet();
        if (!messages.isEmpty()) {
            throw AppException.builder()
                              .badRequest400()
                              .messages(messages)
                              .build();
        }
    }

    public Ping forCreate(ObjectNode objectNode) {
        final Ping.PingBuilder builder = Ping.builder();
        Optional.ofNullable(objectNode.get(iamPUT)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::iamPUT);
        Optional.ofNullable(objectNode.get(correlation)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::correlation);
        Optional.ofNullable(objectNode.get(socpenPUT)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::socpenPUT);

        return builder.build();
    }

    public void validateCreate(final Ping ping) {
        final Set<String> messages = Sets.newHashSet();
        validateCreate(ping, messages);
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public void validateCreate(final Ping ping, final Set<String> messages) {
        if (ping.getIamPUT() == null) {
            messages.add(iamPUT + " mandatory");
        }
    }

    public Map<String, Object> toMap(final Ping ping) {
        return toFilteredMap(ping,
                             ImmutableSet.of(iamPUT, correlation, socpenPUT));
    }

    public Map<String, Object> toFilteredMap(final Ping ping, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        fields.forEach(f -> {
            if (iamPUT.equals(f)) {
                map.put(iamPUT, ping.getIamPUT());
            }
            if (correlation.equals(f)) {
                map.put(correlation, ping.getCorrelation());
            }
            if (socpenPUT.equals(f)) {
                map.put(socpenPUT, ping.getSocpenPUT());
            }
        });
        return map;
    }
}
