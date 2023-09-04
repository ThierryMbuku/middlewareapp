package prosense.sassa.srdclient.sarscompliance.control;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import javax.enterprise.context.Dependent;

import prosense.sassa.srdclient.api.entity.AppException;
import prosense.sassa.srdclient.sarscompliance.entity.SarsCompliance;


import java.util.*;


@Dependent
public class SarsComplianceAgent {
    public static final String outcomeId = "outcomeId";
    public static final String identificationType = "identificationType";
    public static final String identificationNumber = "identificationNumber";
    public static final String countryOfIssue = "countryOfIssue";

    public void validate(final ObjectNode objectNode) {
        final Set<String> messages = Sets.newHashSet();
        Optional.ofNullable(objectNode.get(identificationType)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(identificationType + " invalid");
            } else {
                try {
                    SarsCompliance.IdentificationType.valueOf(n.asText());
                } catch (IllegalArgumentException e) {
                    messages.add(identificationType + " invalid: " + Arrays.toString(SarsCompliance.IdentificationType.values()));
                }
            }
        });
        Optional.ofNullable(objectNode.get(countryOfIssue)).filter(f -> !f.isNull()).ifPresent(n -> {
            if (!n.isTextual()) {
                messages.add(countryOfIssue + " invalid");
            } else {
                if (n.asText().length() > 3) {
                    messages.add(countryOfIssue + " max length 3");
                }
            }
        });
        if (!messages.isEmpty()) {
            throw AppException.builder().badRequest400().messages(messages).build();
        }
    }

    public SarsCompliance forEnquire(ObjectNode objectNode) {
        final SarsCompliance.SarsComplianceBuilder builder = SarsCompliance.builder();
        Optional.ofNullable(objectNode.get(outcomeId)).filter(f -> !f.isNull()).map(JsonNode::asLong).ifPresent(builder::outcomeId);
        Optional.ofNullable(objectNode.get(identificationType)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::identificationType);
        Optional.ofNullable(objectNode.get(identificationNumber)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::identificationNumber);
        Optional.ofNullable(objectNode.get(countryOfIssue)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::countryOfIssue);
        return builder.build();
    }

    public void validateEnquire(final SarsCompliance sarsCompliance) {
        final Set<String> messages = Sets.newHashSet();
        validateEnquire(sarsCompliance, messages);
        if (!messages.isEmpty()) {
            throw AppException.builder().unprocessableEntity422().messages(messages).build();
        }
    }

    public void validateEnquire(final SarsCompliance sarsCompliance, final Set<String> messages) {
        if (sarsCompliance.getOutcomeId() == null) {
            messages.add(outcomeId + " mandatory");
        }
        if (sarsCompliance.getIdentificationType() == null) {
            messages.add(identificationType + " mandatory");
        }
        if (sarsCompliance.getIdentificationNumber() == null) {
            messages.add(identificationNumber + " mandatory");
        }
        if (sarsCompliance.getCountryOfIssue() == null && SarsCompliance.IdentificationType.foreign_passport_number.name().equals(sarsCompliance.getIdentificationType())) {
            messages.add(countryOfIssue + " mandatory");
        }
    }

    public Map<String, Object> toMap(final SarsCompliance sarsCompliance) {
        return toFilteredMap(sarsCompliance, ImmutableSet.of(outcomeId, identificationType, identificationNumber, countryOfIssue));
    }

    public Map<String, Object> toFilteredMap(final SarsCompliance sarsCompliance, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(outcomeId, sarsCompliance.getOutcomeId());
        fields.forEach(f -> {
            if (identificationType.equals(f)) {
                map.put(identificationType, sarsCompliance.getIdentificationType());
            }
            if (identificationNumber.equals(f)) {
                map.put(identificationNumber, sarsCompliance.getIdentificationNumber());
            }
            if (countryOfIssue.equals(f)) {
                map.put(countryOfIssue, sarsCompliance.getCountryOfIssue());
            }
        });
        return map;
    }
}
