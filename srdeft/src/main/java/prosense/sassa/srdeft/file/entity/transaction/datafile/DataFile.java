package prosense.sassa.srdeft.file.entity.transaction.datafile;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Set;
import java.util.List;
import java.util.ArrayList;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class DataFile {
    private ControlHeader controlHeader;
    private InstallationHeader installationHeader;
    private UserHeader userHeader;
    private Set<TransactionRecord> transactionRecords;
    private ContraRecord contraRecord;
    private UserTrailer userTrailer;
    private InstallationTrailer installationTrailer;
    private EOFTrailer eofTrailer;
    
    public List<String> toLines() {
        List<String> lines = new ArrayList<String>();
        lines.add(controlHeader.toString());
        lines.add(installationHeader.toString());
        lines.add(userHeader.toString());
        transactionRecords.forEach(transactionRecord -> {
            lines.add(transactionRecord.toString());
        });
        lines.add(contraRecord.toString());
        lines.add(userTrailer.toString());
        lines.add(installationTrailer.toString());
        lines.add(eofTrailer.toString());
        return lines;
  }
}