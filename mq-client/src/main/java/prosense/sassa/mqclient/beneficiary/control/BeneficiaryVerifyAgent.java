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
import prosense.sassa.mqclient.beneficiary.entity.BeneficiaryVerify;


import javax.enterprise.context.Dependent;

import javax.inject.Inject;

import java.util.*;

import java.util.stream.Collectors;

import org.slf4j.Logger;


@Dependent
public class BeneficiaryVerifyAgent {
    @Inject
    @App
    Logger logger;

    public static final String application = "application";
    public static final String applicantId = "applicantId";
    public static final String applicantFullname = "applicantFullname";
    public static final String applicantSurname = "applicantSurname";
    public static final String status = "status";
    public static final String errorCode = "errorCode";
    public static final String errorDesc = "errorDesc";
    public static final String transaction = "transaction";
    public static final String verified = "verified";
    public static final String override = "override";

    public void validate(final ObjectNode objectNode) {
        final Set<String> messages = Sets.newHashSet();
        if (!messages.isEmpty()) {
            throw AppException.builder()
                              .badRequest400()
                              .messages(messages)
                              .build();
        }
    }

    public BeneficiaryVerify forCreate(ObjectNode objectNode) {
        final BeneficiaryVerify.BeneficiaryVerifyBuilder builder = BeneficiaryVerify.builder();
        Optional.ofNullable(objectNode.get(application)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::application);
        Optional.ofNullable(objectNode.get(applicantId)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::applicantId);
        Optional.ofNullable(objectNode.get(applicantFullname)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::applicantFullname);
        Optional.ofNullable(objectNode.get(applicantSurname)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::applicantSurname);
        Optional.ofNullable(objectNode.get(status)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::status);
        Optional.ofNullable(objectNode.get(errorCode)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::errorCode);
        Optional.ofNullable(objectNode.get(errorDesc)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::errorDesc);
        Optional.ofNullable(objectNode.get(transaction)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::transaction);
        Optional.ofNullable(objectNode.get(verified)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::verified);
        Optional.ofNullable(objectNode.get(override)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::override);
        return builder.build();
    }

    public void validateCreate(final BeneficiaryVerify beneficiaryVerify) {
        final Set<String> messages = Sets.newHashSet();
        if (beneficiaryVerify.getApplication() == null) {
            messages.add(application + " mandatory");
        }
        if (beneficiaryVerify.getApplicantId() == null) {
            messages.add(applicantId + " mandatory");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public Map<String, Object> toMap(final BeneficiaryVerify beneficiaryVerify) {
        return toFilteredMap(beneficiaryVerify, ImmutableSet.of(application, applicantId, applicantFullname, applicantSurname, status, errorCode, errorDesc, transaction, verified, override));
    }

    public Map<String, Object> toFilteredMap(final BeneficiaryVerify beneficiaryVerify, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        fields.forEach(f -> {
            if (application.equals(f)) {
                map.put(application, beneficiaryVerify.getApplication());
            }
            if (applicantId.equals(f)) {
                map.put(applicantId, beneficiaryVerify.getApplicantId());
            }
            if (applicantFullname.equals(f)) {
                map.put(applicantFullname, beneficiaryVerify.getApplicantFullname());
            }
            if (applicantSurname.equals(f)) {
                map.put(applicantSurname, beneficiaryVerify.getApplicantSurname());
            }
            if (status.equals(f)) {
                map.put(status, beneficiaryVerify.getStatus());
            }
            if (errorCode.equals(f)) {
                map.put(errorCode, beneficiaryVerify.getErrorCode());
            }
            if (errorDesc.equals(f)) {
                map.put(errorDesc, beneficiaryVerify.getErrorDesc());
            }
            if (transaction.equals(f)) {
                map.put(transaction, beneficiaryVerify.getTransaction());
            }
            if (verified.equals(f)) {
                map.put(verified, beneficiaryVerify.getVerified());
            }
            if (override.equals(f)) {
                map.put(override, beneficiaryVerify.getOverride());
            }
        });
        return map;
    }
}
