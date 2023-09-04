package prosense.sassa.srdeft.file.entity.transmission;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.ArrayList;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AckFile {
    private String recordIdentifier;
    private String transactionType;
    private String processingDate;
    private String serviceType;
    private String subServiceType;
    private String destination;
    private String originator;
    private String dataDirection;
    private String testLiveIndicator;
    private String fileName;
    private String fileNumber;
    private String errorCode;
    private String filler;
    
    @Override
    public String toString() {
        return recordIdentifier + transactionType + processingDate + serviceType + subServiceType + destination + originator + dataDirection + testLiveIndicator + fileName + fileNumber + errorCode + filler;
  }

    public List<String> toLines() {
        List<String> lines = new ArrayList<String>();
        lines.add(this.toString());
        return lines;
  }
}