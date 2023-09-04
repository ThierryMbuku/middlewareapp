package prosense.sassa.oaamwrapper.transaction.control;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.slf4j.Logger;

import prosense.sassa.oaamwrapper.api.control.App;
import prosense.sassa.oaamwrapper.api.entity.AppException;
import prosense.sassa.oaamwrapper.transaction.entity.Transaction;


import javax.enterprise.context.Dependent;

import java.util.*;

import java.util.stream.Collectors;

import org.slf4j.Logger;


@Dependent
public class TransactionAgent {
    @Inject
    @App
    Logger logger;

    public static final String id = "id";
    public static final String domainUser = "domainUser";
    public static final String socpenUser = "socpenUser";
    public static final String content = "content";
    public static final String detail = "detail";
    public static final String type = "type";
    public static final String challenge = "challenge";
    public static final String state = "state";
    public static final String policy = "policy";
    public static final String status = "status";
    public static final String creator = "creator";
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

    public Transaction forCreate(ObjectNode objectNode) {
        final Transaction.TransactionBuilder builder = Transaction.builder();
        Optional.ofNullable(objectNode.get(id)).filter(f -> !f.isNull()).map(JsonNode::asLong).ifPresent(builder::id);
        Optional.ofNullable(objectNode.get(domainUser)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::domainUser);
        Optional.ofNullable(objectNode.get(socpenUser)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::socpenUser);
        Optional.ofNullable(objectNode.get(content)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::content);
        Optional.ofNullable(objectNode.get(detail)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::detail);
        Optional.ofNullable(objectNode.get(type)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::type);
        Optional.ofNullable(objectNode.get(challenge)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::challenge);
        Optional.ofNullable(objectNode.get(state)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::state);
        Optional.ofNullable(objectNode.get(policy)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::policy);
        Optional.ofNullable(objectNode.get(status)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::status);
        Optional.ofNullable(objectNode.get(creator)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::creator);
        Optional.ofNullable(objectNode.get(updator)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::updator);

        return builder.build();
    }

    public void validateCreate(final Transaction transaction) {
        final Set<String> messages = Sets.newHashSet();
        validateCreate(transaction, messages);
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public void validateCreate(final Transaction transaction, final Set<String> messages) {
        if (transaction.getId() == null) {
            messages.add(id + " mandatory");
        }
    }

    public Map<String, Object> toMap(final Transaction transaction) {
        return toFilteredMap(transaction,
                             ImmutableSet.of(domainUser, socpenUser, content, detail, type, challenge,
                                             state, policy, status, creator, updator));
    }

    public Map<String, Object> toFilteredMap(final Transaction transaction, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(id, transaction.getId());
        fields.forEach(f -> {
            if (domainUser.equals(f)) {
                map.put(domainUser, transaction.getDomainUser());
            }
            if (socpenUser.equals(f)) {
                map.put(socpenUser, transaction.getSocpenUser());
            }
            if (content.equals(f)) {
                map.put(content, transaction.getContent());
            }
            if (detail.equals(f)) {
                map.put(detail, transaction.getDetail());
            }
            if (type.equals(f)) {
                map.put(type, transaction.getType());
            }
            if (challenge.equals(f)) {
                map.put(challenge, transaction.getChallenge());
            }
            if (state.equals(f)) {
                map.put(state, transaction.getState());
            }
            if (policy.equals(f)) {
                map.put(policy, transaction.getPolicy());
            }
            if (status.equals(f)) {
                map.put(status, transaction.getStatus());
            }
            if (creator.equals(f)) {
                map.put(creator, transaction.getCreator());
            }
            if (updator.equals(f)) {
                map.put(updator, transaction.getUpdator());
            }
        });
        return map;
    }
}
