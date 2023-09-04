package prosense.sassa.srdeft.file.entity.transaction.datafile;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TransactionRecord {
    private String recordIdentifier;
    private String userBranch;
    private String userNominatedAccountNumber;
    private String userCode;
    private String userSequenceNumber;
    private String homingBranch;
    private String homingAccountNumber;
    private String typeOfAccount;
    private String amount;
    private String actionDate;
    private String entryClass;
    private String taxCode;
    private String filler1;
    private String filler2;
    private String userReference;
    private String homingAccountName;
    private String nonStandardHomingAccountNumber;
    private String filler3;
    private String homingInstitution;
    private String filler4;
    
    @Override
    public String toString() {
        return recordIdentifier + userBranch + userNominatedAccountNumber + userCode + userSequenceNumber + homingBranch + homingAccountNumber + typeOfAccount + amount + actionDate + entryClass + taxCode + filler1 + filler2 + userReference + homingAccountName + nonStandardHomingAccountNumber + filler3 + homingInstitution + filler4;
  }
}