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
public class DetailLine0 {
    private String lineIdentifier;
    private String departmentCode;
    private String paymentMethod;
    private String serialNumber;
    private String cashbookNumber;
    private String issueDate;
    private String sign;
    private String totalAmount;
    private String beneficiaryName;
    private String actionDate;
    private String persalFlag;
    private String entitySystemNumber;
    private String bankAccountNumber;
    private String bankCode;
    private String bankBranchName;
    private String bankBranchNumber;
    private String bankAccountType;
    private String bankAccountHolderName;
    
    @Override
    public String toString() {
        return lineIdentifier + departmentCode + paymentMethod + serialNumber + cashbookNumber + issueDate + sign + totalAmount + beneficiaryName + actionDate + persalFlag + entitySystemNumber + bankAccountNumber + bankCode + bankBranchName + bankBranchNumber + bankAccountType + bankAccountHolderName;
  }
}