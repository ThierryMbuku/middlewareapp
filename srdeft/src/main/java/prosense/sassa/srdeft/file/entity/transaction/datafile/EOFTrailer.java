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
public class EOFTrailer {
    private String recordIdentifier;
    private String processingDate;
    private String serviceType;
    private String subServiceType;
    private String destination;
    private String numberOfRecords;
    private String sourceIdentifier;
    private String encryptedWorkingKey16;
    private String macOfHashTotal;
    private String hashTotal;
    private String encryptedWorkingKey32;
    private String filler;
    
    @Override
    public String toString() {
        return recordIdentifier + processingDate + serviceType + subServiceType + destination + numberOfRecords + sourceIdentifier + encryptedWorkingKey16 + macOfHashTotal + hashTotal + encryptedWorkingKey32 + filler;
  }
}