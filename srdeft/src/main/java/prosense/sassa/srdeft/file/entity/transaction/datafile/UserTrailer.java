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
public class UserTrailer {
    private String recordIdentifier;
    private String userCode;
    private String firstSequenceNumber;
    private String lastSequenceNumber;
    private String firstActionDate;
    private String lastActionDate;
    private String noOfDebitRecords;
    private String noOfCreditRecords;
    private String noOfContraRecords;
    private String totalDebitValue;
    private String totalCreditValue;
    private String hashTotalOfHomingAccountNumbers;
    private String filler;
    
    @Override
    public String toString() {
        return recordIdentifier + userCode + firstSequenceNumber + lastSequenceNumber + firstActionDate + lastActionDate + noOfDebitRecords + noOfCreditRecords + noOfContraRecords + totalDebitValue + totalCreditValue + hashTotalOfHomingAccountNumbers + filler;
  }
}