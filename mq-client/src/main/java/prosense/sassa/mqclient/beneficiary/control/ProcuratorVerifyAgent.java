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
import prosense.sassa.mqclient.beneficiary.entity.ProcuratorVerify;


import javax.enterprise.context.Dependent;

import javax.inject.Inject;

import java.util.*;

import java.util.stream.Collectors;

import org.slf4j.Logger;


@Dependent
public class ProcuratorVerifyAgent {
    @Inject
    @App
    Logger logger;

    public static final String application = "application";
    public static final String applicantId = "applicantId";
    public static final String procuratorId = "procuratorId";
    public static final String procuratorFullname = "procuratorFullname";
    public static final String procuratorSurname = "procuratorSurname";
    public static final String status = "status";
    public static final String errorCode = "errorCode";
    public static final String errorDesc = "errorDesc";
    public static final String transaction = "transaction";
    public static final String verified = "verified";

    public void validate(final ObjectNode objectNode) {
        final Set<String> messages = Sets.newHashSet();
        if (!messages.isEmpty()) {
            throw AppException.builder()
                              .badRequest400()
                              .messages(messages)
                              .build();
        }
    }

    public ProcuratorVerify forCreate(ObjectNode objectNode) {
        final ProcuratorVerify.ProcuratorVerifyBuilder builder = ProcuratorVerify.builder();
        Optional.ofNullable(objectNode.get(application)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::application);
        Optional.ofNullable(objectNode.get(applicantId)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::applicantId);
        Optional.ofNullable(objectNode.get(procuratorId)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::procuratorId);
        Optional.ofNullable(objectNode.get(procuratorFullname)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::procuratorFullname);
        Optional.ofNullable(objectNode.get(procuratorSurname)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::procuratorSurname);
        Optional.ofNullable(objectNode.get(status)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::status);
        Optional.ofNullable(objectNode.get(errorCode)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::errorCode);
        Optional.ofNullable(objectNode.get(errorDesc)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::errorDesc);
        Optional.ofNullable(objectNode.get(transaction)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::transaction);
        Optional.ofNullable(objectNode.get(verified)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::verified);
        return builder.build();
    }

    public void validateCreate(final ProcuratorVerify procuratorVerify) {
        final Set<String> messages = Sets.newHashSet();
        if (procuratorVerify.getApplication() == null) {
            messages.add(application + " mandatory");
        }
        if (procuratorVerify.getApplicantId() == null) {
            messages.add(applicantId + " mandatory");
        }
        if (procuratorVerify.getProcuratorId() == null) {
            messages.add(procuratorId + " mandatory");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public Map<String, Object> toMap(final ProcuratorVerify procuratorVerify) {
        return toFilteredMap(procuratorVerify, ImmutableSet.of(application, applicantId, procuratorId, procuratorFullname, procuratorSurname, status, errorCode, errorDesc, transaction, verified));
    }

    public Map<String, Object> toFilteredMap(final ProcuratorVerify procuratorVerify, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        fields.forEach(f -> {
            if (application.equals(f)) {
                map.put(application, procuratorVerify.getApplication());
            }
            if (applicantId.equals(f)) {
                map.put(applicantId, procuratorVerify.getApplicantId());
            }
            if (procuratorId.equals(f)) {
                map.put(procuratorId, procuratorVerify.getProcuratorId());
            }
            if (procuratorFullname.equals(f)) {
                map.put(procuratorFullname, procuratorVerify.getProcuratorFullname());
            }
            if (procuratorSurname.equals(f)) {
                map.put(procuratorSurname, procuratorVerify.getProcuratorSurname());
            }
            if (status.equals(f)) {
                map.put(status, procuratorVerify.getStatus());
            }
            if (errorCode.equals(f)) {
                map.put(errorCode, procuratorVerify.getErrorCode());
            }
            if (errorDesc.equals(f)) {
                map.put(errorDesc, procuratorVerify.getErrorDesc());
            }
            if (transaction.equals(f)) {
                map.put(transaction, procuratorVerify.getTransaction());
            }
            if (verified.equals(f)) {
                map.put(verified, procuratorVerify.getVerified());
            }
        });
        return map;
    }
}
