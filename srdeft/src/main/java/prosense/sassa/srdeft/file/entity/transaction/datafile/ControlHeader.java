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
public class ControlHeader {
    private String recordIdentifier;
    private String processingDate;
    private String serviceType;
    private String subServiceType;
    private String destination;
    private String originator;
    private String fileName;
    private String fileNumber;
    private String dataType;
    private String dataDirection;
    private String settlementDate;
    private String testLiveIndicator;
    private String recordSize;
    private String bankCode;
    private String reportType;
    private String fileType;
    private String settlementWindow;
    private String transactionType;
    private String filler;
    
    @Override
    public String toString() {
        return recordIdentifier + processingDate + serviceType + subServiceType + destination + originator + fileName + fileNumber + dataType + dataDirection + settlementDate + testLiveIndicator + recordSize + bankCode + reportType + fileType + settlementWindow + transactionType + filler;
  }
}