package prosense.sassa.dataloader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = { "id" })
@ToString
@JsonInclude(Include.NON_NULL)
public class EnrolmentParty {
    private Long id;
    private String idType;
    private String idNumber;
    private String firstname;
    private String surname;
    private String email;
    private String mobile;
    private String membership;
    private String branch;
    private String status;
    private String domainUser;
    private String socpenUser;
    private String region;
    private String district;
    private String position;
    private String base;
    private Boolean supervisor;
    private Boolean enrolment;
    private Boolean enrolmentAdmin;
    private Boolean beneficiary;
    private Boolean beneficiaryAdmin;
    private Boolean cardswop;
    private Boolean nonrepudiation;
    private Boolean support;
    private Boolean supportAdmin;
    private Boolean approver;
    private String created;
    private String creator;
    private String updated;
    private String updator;
}
