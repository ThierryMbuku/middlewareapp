package prosense.sassa.sapo.verification.control;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import org.slf4j.Logger;

import prosense.sassa.sapo.api.control.App;
import prosense.sassa.sapo.api.entity.AppException;
import prosense.sassa.sapo.verification.entity.Verification;
import prosense.sassa.sapo.beneficiaryapi.entity.Fingerprint;
import prosense.sassa.sapo.businessrules.fingerprint.control.FingerprintAgent;

import javax.enterprise.context.Dependent;

import javax.inject.Inject;

import java.util.*;
import java.util.stream.Collectors;

import org.slf4j.Logger;


@Dependent
public class VerificationAgent {
    @Inject
    @App
    Logger logger;

    @Inject
    FingerprintAgent fingerprintAgent;

    public static final String idNumber = "idNumber";
    public static final String fullname = "fullname";
    public static final String surname = "surname";
    public static final String status = "status";
    public static final String transaction = "transaction";
    public static final String code = "code";
    public static final String description = "description";
    public static final String fingerprints = "fingerprints";

    public void validate(final ObjectNode objectNode) {
        final Set<String> messages = Sets.newHashSet();
        if (!messages.isEmpty()) {
            throw AppException.builder()
                              .badRequest400()
                              .messages(messages)
                              .build();
        }
    }

    public Verification forCreate(ObjectNode objectNode) {
        final Verification.VerificationBuilder builder = Verification.builder();
        Optional.ofNullable(objectNode.get(idNumber)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::idNumber);
        Optional.ofNullable(objectNode.get(fullname)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::fullname);
        Optional.ofNullable(objectNode.get(surname)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::surname);
        if (objectNode.get(fingerprints) != null) {
            Set<Fingerprint> fingerprintSet = new HashSet<Fingerprint>();
            Iterator<JsonNode> iterator = objectNode.get(fingerprints).iterator();
            while (iterator.hasNext()) {
                fingerprintSet.add(fingerprintAgent.forCreate((ObjectNode) iterator.next()));
            }
            builder.fingerprints(fingerprintSet);
        }

        return builder.build();
    }

    public void validateCreate(final Verification verification) {
        final Set<String> messages = Sets.newHashSet();
        if (verification.getIdNumber() == null) {
            messages.add(idNumber + " mandatory");
        }
        if (verification.getFingerprints() != null && verification.getFingerprints().size() < 2) {
            messages.add(fingerprints + " minimum 2 required");
        }
        if (!messages.isEmpty()) {
            throw AppException.builder()
                              .unprocessableEntity422()
                              .messages(messages)
                              .build();
        }
    }

    public Map<String, Object> toMap(final Verification verification) {
        return toFilteredMap(verification, ImmutableSet.of(idNumber, fullname, surname, status, transaction, code, description, fingerprints));
    }

    public Map<String, Object> toFilteredMap(final Verification verification, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        fields.forEach(f -> {
            if (idNumber.equals(f)) {
                map.put(idNumber, verification.getIdNumber());
            }
            if (fullname.equals(f)) {
                map.put(fullname, verification.getFullname());
            }
            if (surname.equals(f)) {
                map.put(surname, verification.getSurname());
            }
            if (status.equals(f)) {
                map.put(status, verification.getStatus());
            }
            if (transaction.equals(f)) {
                map.put(transaction, verification.getTransaction());
            }
            if (code.equals(f)) {
                map.put(code, verification.getCode());
            }
            if (description.equals(f)) {
                map.put(description, verification.getDescription());
            }
            if (fingerprints.equals(f) && verification.getFingerprints() != null) {
                map.put(fingerprints, verification.getFingerprints()
                                   .stream()
                                   .map(fingerprint -> fingerprintAgent.toMap(fingerprint))
                                   .collect(Collectors.toCollection(LinkedHashSet::new)));
            }
        });
        return map;
    }
}
