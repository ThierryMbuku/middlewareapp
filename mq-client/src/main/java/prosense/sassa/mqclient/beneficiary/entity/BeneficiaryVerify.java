package prosense.sassa.mqclient.beneficiary.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = { "application", "applicantId" })
@ToString
public class BeneficiaryVerify {
    private String application;
    private String applicantId;
    private String applicantFullname;
    private String applicantSurname;
    private String status;
    private String errorCode;
    private String errorDesc;
    private String transaction;
    private String verified;
    private String override;
}
