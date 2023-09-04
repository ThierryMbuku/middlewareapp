package prosense.sassa.srdeft.file.entity.transaction.cashbook;

import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.ArrayList;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class CashBook {
    private HeaderRecord headerRecord;
    private DetailLine0 detailLine0;
    private DetailLine1 detailLine1;
    private TrailerRecord trailerRecord;
    
    public List<String> toLines() {
        List<String> lines = new ArrayList<String>();
        lines.add(headerRecord.toString());
        lines.add(detailLine0.toString());
        lines.add(detailLine1.toString());
        lines.add(trailerRecord.toString());
        return lines;
  }
}