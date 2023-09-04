package prosense.sassa.srdeft.cashbook.control;

import com.google.common.collect.ImmutableSet;

import prosense.sassa.srdeft.cashbook.entity.CashBook;

import javax.enterprise.context.Dependent;

import java.util.*;
import java.util.stream.Collectors;

import javax.inject.Inject;


@Dependent
public class CashBookAgent {
    @Inject
    DataFileAgent dataFileAgent;

    public static final String id = "id";
    public static final String covJob = "covJob";
    public static final String fileName = "fileName";
    public static final String serialNumber = "serialNumber";
    public static final String departmentCode = "departmentCode";
    public static final String issueDate = "issueDate";
    public static final String paymentAmount = "paymentAmount";
    public static final String paymentDescription = "paymentDescription";
    public static final String dataFiles = "dataFiles";
    public static final String creator = "creator";
    public static final String created = "created";

    public Map<String, Object> toMap(final CashBook cashBook) {
        return toFilteredMap(cashBook, ImmutableSet.of(covJob, fileName, serialNumber, departmentCode, issueDate, paymentAmount, paymentDescription, dataFiles, creator, created));
    }

    public Map<String, Object> toFilteredMap(final CashBook cashBook, final Iterable<String> fields) {
        final Map<String, Object> map = new LinkedHashMap<>();
        map.put(id, cashBook.getId());
        fields.forEach(f -> {
            if (covJob.equals(f)) {
                map.put(covJob, cashBook.getCovJob());
            }
            if (fileName.equals(f)) {
                map.put(fileName, cashBook.getFileName());
            }
            if (serialNumber.equals(f)) {
                map.put(serialNumber, cashBook.getSerialNumber());
            }
            if (departmentCode.equals(f)) {
                map.put(departmentCode, cashBook.getDepartmentCode());
            }
            if (issueDate.equals(f)) {
                map.put(issueDate, cashBook.getIssueDate());
            }
            if (paymentAmount.equals(f)) {
                map.put(paymentAmount, cashBook.getPaymentAmount());
            }
            if (paymentDescription.equals(f)) {
                map.put(paymentDescription, cashBook.getPaymentDescription());
            }
            if (dataFiles.equals(f) && cashBook.getDataFiles() != null) {
                map.put(dataFiles, cashBook.getDataFiles().stream()
                                   .map(dataFile -> dataFileAgent.toMap(dataFile))
                                   .collect(Collectors.toCollection(LinkedHashSet::new)));
            }
            if (creator.equals(f)) {
                map.put(creator, cashBook.getCreator());
            }
            if (created.equals(f)) {
                map.put(created, cashBook.getCreated());
            }
        });
        return map;
    }
}
