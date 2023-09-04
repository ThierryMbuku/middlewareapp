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
import prosense.sassa.mqclient.beneficiary.entity.BeneficiaryEnrol;


import javax.enterprise.context.Dependent;

import javax.inject.Inject;

import java.util.*;

import java.util.stream.Collectors;

import org.slf4j.Logger;


@Dependent
public class BeneficiaryEnrolAgent {
    @Inject
    @App
    Logger logger;

    public static final String application = "application";
    public static final String applicantId = "applicantId";
    public static final String captureStatus = "captureStatus";
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

    public BeneficiaryEnrol forCreate(ObjectNode objectNode) {
        final BeneficiaryEnrol.BeneficiaryEnrolBuilder builder = BeneficiaryEnrol.builder();
        Optional.ofNullable(objectNode.get(application)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::application);
        Optional.ofNullable(objectNode.get(applicantId)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::applicantId);
        Optional.ofNullable(objectNode.get(captureStatus)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::captureStatus);
        Optional.ofNullable(objectNode.get(override)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::override);
        return builder.build();
    }

    public void validateCreate(final BeneficiaryEnrol beneficiaryEnrol) {
        final Set<String> messages = Sets.newHashSet();
        if (beneficiaryEnrol.getApplication() == null) {
            messages.add(application + " mandatory");
        }
        if (beneficiaryEnrol.getApplicantId() == null) {
            messages.add(applicantId + " mandatory");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public Map<String, Object> toMap(final BeneficiaryEnrol beneficiaryEnrol) {
        return toFilteredMap(beneficiaryEnrol, ImmutableSet.of(application, applicantId, captureStatus, override));
    }

    public Map<String, Object> toFilteredMap(final BeneficiaryEnrol beneficiaryEnrol, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        fields.forEach(f -> {
            if (application.equals(f)) {
                map.put(application, beneficiaryEnrol.getApplication());
            }
            if (applicantId.equals(f)) {
                map.put(applicantId, beneficiaryEnrol.getApplicantId());
            }
            if (captureStatus.equals(f)) {
                map.put(captureStatus, beneficiaryEnrol.getCaptureStatus());
            }
            if (override.equals(f)) {
                map.put(override, beneficiaryEnrol.getOverride());
            }
        });
        return map;
    }
}
