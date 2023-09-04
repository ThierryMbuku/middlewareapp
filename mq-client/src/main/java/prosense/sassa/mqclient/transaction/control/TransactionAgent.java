package prosense.sassa.mqclient.transaction.control;

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
import prosense.sassa.mqclient.transaction.entity.Transaction;


import javax.enterprise.context.Dependent;

import javax.inject.Inject;

import java.util.*;

import java.util.stream.Collectors;

import org.slf4j.Logger;


@Dependent
public class TransactionAgent {
    @Inject
    @App
    Logger logger;

    public static final String id = "id";
    public static final String correlation = "correlation";
    public static final String domainUser = "domainUser";
    public static final String socpenUser = "socpenUser";
    public static final String content = "content";
    public static final String detail = "detail";
    public static final String type = "type";
    public static final String challenge = "challenge";
    public static final String hash = "hash";
    public static final String state = "state";
    public static final String policy = "policy";
    public static final String status = "status";
    public static final String cipher = "cipher";
    public static final String choice = "choice";
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

    public Transaction forCreate(ObjectNode objectNode) {
        final Transaction.TransactionBuilder builder = Transaction.builder();
        Optional.ofNullable(objectNode.get(id)).filter(f -> !f.isNull()).map(JsonNode::asLong).ifPresent(builder::id);
        Optional.ofNullable(objectNode.get(correlation)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::correlation);
        Optional.ofNullable(objectNode.get(domainUser)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::domainUser);
        Optional.ofNullable(objectNode.get(socpenUser)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::socpenUser);
        Optional.ofNullable(objectNode.get(content)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::content);
        Optional.ofNullable(objectNode.get(detail)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::detail);
        Optional.ofNullable(objectNode.get(type)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::type);
        Optional.ofNullable(objectNode.get(challenge)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::challenge);
        Optional.ofNullable(objectNode.get(hash)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::hash);
        Optional.ofNullable(objectNode.get(state)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::state);
        Optional.ofNullable(objectNode.get(policy)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::policy);
        Optional.ofNullable(objectNode.get(status)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::status);
        Optional.ofNullable(objectNode.get(cipher)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::cipher);
        Optional.ofNullable(objectNode.get(choice)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::choice);
        Optional.ofNullable(objectNode.get(created)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::created);
        Optional.ofNullable(objectNode.get(creator)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::creator);
        Optional.ofNullable(objectNode.get(updated)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::updated);
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
        if (transaction.getCorrelation() == null) {
            messages.add(correlation + " mandatory");
        }
    }

    public Map<String, Object> toMap(final Transaction transaction) {
        return toFilteredMap(transaction,
                             ImmutableSet.of(correlation, domainUser, socpenUser, content, detail, type, challenge, hash,
                                             state, policy, status, cipher, choice, created, creator, updated, updator));
    }

    public Map<String, Object> toFilteredMap(final Transaction transaction, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(id, transaction.getId());
        fields.forEach(f -> {
            if (correlation.equals(f)) {
                map.put(correlation, transaction.getCorrelation());
            }
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
            if (hash.equals(f)) {
                map.put(hash, transaction.getHash());
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
            if (cipher.equals(f)) {
                map.put(cipher, transaction.getCipher());
            }
            if (choice.equals(f)) {
                map.put(choice, transaction.getChoice());
            }
            if (created.equals(f)) {
                map.put(created, transaction.getCreated());
            }
            if (creator.equals(f)) {
                map.put(creator, transaction.getCreator());
            }
            if (updated.equals(f)) {
                map.put(updated, transaction.getUpdated());
            }
            if (updator.equals(f)) {
                map.put(updator, transaction.getUpdator());
            }
        });
        return map;
    }
}
