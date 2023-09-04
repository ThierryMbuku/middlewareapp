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
public class InstallationHeader {
    private String recordIdentifier;
    private String volumeNumber;
    private String serialNumber;
    private String installationIDCodeFrom;
    private String installationIDCodeTo;
    private String creationDate;
    private String purgeDate;
    private String installationGenerationNumber;
    private String blockLength;
    private String recordLength;
    private String service;
    private String filler1;
    private String filler2;
    
    @Override
    public String toString() {
        return recordIdentifier + volumeNumber + serialNumber + installationIDCodeFrom + installationIDCodeTo + creationDate + purgeDate + installationGenerationNumber + blockLength + recordLength + service + filler1 + filler2;
  }
}