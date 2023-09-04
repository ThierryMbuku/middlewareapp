package prosense.sassa.srdeft.cashbook.control;

import com.google.common.collect.ImmutableSet;

import prosense.sassa.srdeft.cashbook.entity.DataFile;

import javax.enterprise.context.Dependent;

import java.util.*;
import java.util.stream.Collectors;

import javax.inject.Inject;


@Dependent
public class DataFileAgent {
    @Inject
    PatchOutcomeErrorAgent patchOutcomeErrorAgent;

    public static final String id = "id";
    public static final String cashBook = "cashBook";
    public static final String fileName = "fileName";
    public static final String fileNumber = "fileNumber";
    public static final String installationGenerationNumber = "installationGenerationNumber";
    public static final String userGenerationNumber = "userGenerationNumber";
    public static final String serialNumber = "serialNumber";
    public static final String numberOfRecords = "numberOfRecords";
    public static final String recordCount = "recordCount";
    public static final String firstSequenceNumber = "firstSequenceNumber";
    public static final String lastSequenceNumber = "lastSequenceNumber";
    public static final String totalDebitValue = "totalDebitValue";
    public static final String totalCreditValue = "totalCreditValue";
    public static final String userCode = "userCode";
    public static final String userBranch = "userBranch";
    public static final String userNominatedAccountNumber = "userNominatedAccountNumber";
    public static final String actionDate = "actionDate";
    public static final String testLiveIndicator = "testLiveIndicator";
    public static final String settlementDate = "settlementDate";
    public static final String patchOutcomeErrors = "patchOutcomeErrors";
    public static final String creator = "creator";
    public static final String created = "created";

    public Map<String, Object> toMap(final DataFile dataFile) {
        return toFilteredMap(dataFile, ImmutableSet.of(cashBook, fileName, fileNumber, installationGenerationNumber, userGenerationNumber, serialNumber, numberOfRecords, recordCount, firstSequenceNumber, lastSequenceNumber, totalDebitValue, totalCreditValue, userCode, userBranch, userNominatedAccountNumber, actionDate, testLiveIndicator, settlementDate, patchOutcomeErrors, creator, created));
    }

    public Map<String, Object> toFilteredMap(final DataFile dataFile, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(id, dataFile.getId());
        fields.forEach(f -> {
            if (cashBook.equals(f)) {
                map.put(cashBook, dataFile.getCashBook());
            }
            if (fileName.equals(f)) {
                map.put(fileName, dataFile.getFileName());
            }
            if (fileNumber.equals(f)) {
                map.put(fileNumber, dataFile.getFileNumber());
            }
            if (installationGenerationNumber.equals(f)) {
                map.put(installationGenerationNumber, dataFile.getInstallationGenerationNumber());
            }
            if (userGenerationNumber.equals(f)) {
                map.put(userGenerationNumber, dataFile.getUserGenerationNumber());
            }
            if (serialNumber.equals(f)) {
                map.put(serialNumber, dataFile.getSerialNumber());
            }
            if (numberOfRecords.equals(f)) {
                map.put(numberOfRecords, dataFile.getNumberOfRecords());
            }
            if (recordCount.equals(f)) {
                map.put(recordCount, dataFile.getRecordCount());
            }
            if (firstSequenceNumber.equals(f)) {
                map.put(firstSequenceNumber, dataFile.getFirstSequenceNumber());
            }
            if (lastSequenceNumber.equals(f)) {
                map.put(lastSequenceNumber, dataFile.getLastSequenceNumber());
            }
            if (totalDebitValue.equals(f)) {
                map.put(totalDebitValue, dataFile.getTotalDebitValue());
            }
            if (totalCreditValue.equals(f)) {
                map.put(totalCreditValue, dataFile.getTotalCreditValue());
            }
            if (userCode.equals(f)) {
                map.put(userCode, dataFile.getUserCode());
            }
            if (userBranch.equals(f)) {
                map.put(userBranch, dataFile.getUserBranch());
            }
            if (userNominatedAccountNumber.equals(f)) {
                map.put(userNominatedAccountNumber, dataFile.getUserNominatedAccountNumber());
            }
            if (actionDate.equals(f)) {
                map.put(actionDate, dataFile.getActionDate());
            }
            if (testLiveIndicator.equals(f)) {
                map.put(testLiveIndicator, dataFile.getTestLiveIndicator());
            }
            if (settlementDate.equals(f)) {
                map.put(settlementDate, dataFile.getSettlementDate());
            }
            if (patchOutcomeErrors.equals(f) && dataFile.getPatchOutcomeErrors() != null) {
                map.put(patchOutcomeErrors, dataFile.getPatchOutcomeErrors().stream()
                                   .map(patchOutcomeError -> patchOutcomeErrorAgent.toMap(patchOutcomeError))
                                   .collect(Collectors.toCollection(LinkedHashSet::new)));
            }
            if (creator.equals(f)) {
                map.put(creator, dataFile.getCreator());
            }
            if (created.equals(f)) {
                map.put(created, dataFile.getCreated());
            }
        });
        return map;
    }
}
