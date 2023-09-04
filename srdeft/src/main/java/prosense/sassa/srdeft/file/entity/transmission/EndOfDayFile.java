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
public class EndOfDayFile {
    private String recordIdentifier;
    private String transactionType;
    private String processingDate;
    private String serviceType;
    private String subServiceType;
    private String destination;
    private String originator;
    private String dataDirection;
    private String testLiveIndicator;
    private String filler1;
    private String numberOfFiles;
    private String numberOfRecords;
    private String filler2;
    
    @Override
    public String toString() {
        return recordIdentifier + transactionType + processingDate + serviceType + subServiceType + destination + originator + dataDirection + testLiveIndicator + filler1 + numberOfFiles + numberOfRecords + filler2;
  }

    public List<String> toLines() {
        List<String> lines = new ArrayList<String>();
        lines.add(this.toString());
        return lines;
  }
}