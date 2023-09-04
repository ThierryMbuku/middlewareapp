package prosense.sassa.srdeft.file.entity.transaction.cashbook;

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
public class TrailerRecord {
    private String lineIdentifier;
    private String recordCount;
    private String sign;
    private String hashTotal;
    private String filler;
    
    @Override
    public String toString() {
        return lineIdentifier + recordCount + sign + hashTotal + filler;
  }
}