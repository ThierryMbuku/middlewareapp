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
public class UserHeader {
    private String recordIdentifier;
    private String userCode;
    private String creationDate;
    private String purgeDate;
    private String firstActionDate;
    private String lastActionDate;
    private String firstSequenceNumber;
    private String userGenerationNumber;
    private String typeOfService;
    private String filler;
    
    @Override
    public String toString() {
        return recordIdentifier + userCode + creationDate + purgeDate + firstActionDate + lastActionDate + firstSequenceNumber + userGenerationNumber + typeOfService + filler;
  }
}