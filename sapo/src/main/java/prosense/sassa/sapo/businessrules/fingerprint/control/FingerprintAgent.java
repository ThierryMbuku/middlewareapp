package prosense.sassa.sapo.businessrules.fingerprint.control;

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
import prosense.sassa.sapo.beneficiaryapi.entity.Fingerprint;


import javax.enterprise.context.Dependent;

import javax.inject.Inject;

import java.util.*;

import org.slf4j.Logger;


@Dependent
public class FingerprintAgent {
    @Inject
    @App
    Logger logger;

    public static final String id = "id";
    public static final String party = "party";
    public static final String hand = "hand";
    public static final String finger = "finger";
    public static final String grade = "grade";
    public static final String template = "template";
    public static final String image = "image";
    public static final String imageType = "imageType";
    public static final String created = "created";
    public static final String creator = "creator";
    public static final String updated = "updated";
    public static final String updator = "updator";

    public Fingerprint forCreate(ObjectNode objectNode) {
        final Fingerprint.FingerprintBuilder builder = Fingerprint.builder();
        Optional.ofNullable(objectNode.get(party)).filter(f -> !f.isNull()).map(JsonNode::asLong).ifPresent(builder::party);
        Optional.ofNullable(objectNode.get(hand)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::hand);
        Optional.ofNullable(objectNode.get(finger)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::finger);
        Optional.ofNullable(objectNode.get(grade)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::grade);
        Optional.ofNullable(objectNode.get(template)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::template);
        Optional.ofNullable(objectNode.get(image)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::image);
        Optional.ofNullable(objectNode.get(imageType)).filter(f -> !f.isNull()).map(JsonNode::asText).ifPresent(builder::imageType);
        return builder.build();
    }

    public Map<String, Object> toMap(final Fingerprint fingerprint) {
        return toFilteredMap(fingerprint, ImmutableSet.of(party, hand, finger, grade, template, image, imageType, created, creator, updated, updator));
    }

    public Map<String, Object> toFilteredMap(final Fingerprint fingerprint, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        fields.forEach(f -> {
            if (id.equals(f)) {
                map.put(id, fingerprint.getId());
            }
            if (party.equals(f)) {
                map.put(party, fingerprint.getParty());
            }
            if (hand.equals(f)) {
                map.put(hand, fingerprint.getHand());
            }
            if (finger.equals(f)) {
                map.put(finger, fingerprint.getFinger());
            }
            if (grade.equals(f)) {
                map.put(grade, fingerprint.getGrade());
            }
            if (template.equals(f)) {
                map.put(template, fingerprint.getTemplate());
            }
            if (image.equals(f)) {
                map.put(image, fingerprint.getImage());
            }
            if (imageType.equals(f)) {
                map.put(imageType, fingerprint.getImageType());
            }
            if (created.equals(f)) {
                map.put(created, fingerprint.getCreated());
            }
            if (creator.equals(f)) {
                map.put(creator, fingerprint.getCreator());
            }
            if (updated.equals(f)) {
                map.put(updated, fingerprint.getUpdated());
            }
            if (updator.equals(f)) {
                map.put(updator, fingerprint.getUpdator());
            }
        });
        return map;
    }
}
