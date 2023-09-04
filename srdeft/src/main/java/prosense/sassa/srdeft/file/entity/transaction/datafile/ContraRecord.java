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
public class ContraRecord {
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
    private String filler1;
    private String userReference;
    private String nominatedAccountName;
    private String filler2;
    
    @Override
    public String toString() {
        return recordIdentifier + userBranch + userNominatedAccountNumber + userCode + userSequenceNumber + homingBranch + homingAccountNumber + typeOfAccount + amount + actionDate + entryClass + filler1 + userReference + nominatedAccountName + filler2;
  }
}