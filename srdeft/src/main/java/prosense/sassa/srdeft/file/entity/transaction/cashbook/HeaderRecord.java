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
public class HeaderRecord {
    private String sourceSystem;
    private String dateCreated;
    private String timeCreated;
    private String serialNumber;
    private String atcSourceSerialNumber;
    private String dateInterfaced;
    private String cashDate;
    private String filler;
    
    @Override
    public String toString() {
        return sourceSystem + dateCreated + timeCreated  + serialNumber  + atcSourceSerialNumber + dateInterfaced + cashDate + filler;
  }
}