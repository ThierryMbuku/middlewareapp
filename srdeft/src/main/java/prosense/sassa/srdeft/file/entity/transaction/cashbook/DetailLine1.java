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
public class DetailLine1 {
    private String lineIdentifier;
    private String cashBookNumber;
    private String paymentOrigin;
    private String paymentNumber;
    private String sourceDocumentType;
    private String sourceDocumentNumber;
    private String purchaseOrderNumber;
    private String sign;
    private String paymentAmount;
    private String paymentDescription;
    private String filler;
    
    @Override
    public String toString() {
        return lineIdentifier + cashBookNumber + paymentOrigin + paymentNumber + sourceDocumentType + sourceDocumentNumber + purchaseOrderNumber + sign + paymentAmount + paymentDescription + filler;
  }
}