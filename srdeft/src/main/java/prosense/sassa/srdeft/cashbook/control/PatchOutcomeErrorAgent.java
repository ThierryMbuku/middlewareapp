package prosense.sassa.srdeft.cashbook.control;

import com.google.common.collect.ImmutableSet;

import prosense.sassa.srdeft.cashbook.entity.PatchOutcomeError;

import javax.enterprise.context.Dependent;

import java.util.*;


@Dependent
public class PatchOutcomeErrorAgent {
    public static final String id = "id";
    public static final String dataFile = "dataFile";
    public static final String outcome = "outcome";
    public static final String type = "type";
    public static final String error = "error";
    public static final String creator = "creator";
    public static final String created = "created";

    public Map<String, Object> toMap(final PatchOutcomeError patchOutcomeError) {
        return toFilteredMap(patchOutcomeError, ImmutableSet.of(dataFile, outcome, type, error, creator, created));
    }

    public Map<String, Object> toFilteredMap(final PatchOutcomeError patchOutcomeError, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(id, patchOutcomeError.getId());
        fields.forEach(f -> {
            if (dataFile.equals(f)) {
                map.put(dataFile, patchOutcomeError.getDataFile());
            }
            if (outcome.equals(f)) {
                map.put(outcome, patchOutcomeError.getOutcome());
            }
            if (type.equals(f)) {
                map.put(type, patchOutcomeError.getType());
            }
            if (error.equals(f)) {
                map.put(error, patchOutcomeError.getError());
            }
            if (creator.equals(f)) {
                map.put(creator, patchOutcomeError.getCreator());
            }
            if (created.equals(f)) {
                map.put(created, patchOutcomeError.getCreated());
            }
        });
        return map;
    }
}
