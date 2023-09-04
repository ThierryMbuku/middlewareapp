package prosense.sassa.srdeft.userdetail.entity;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
@EqualsAndHashCode(of = { "id" })
@ToString
@Entity
@Cacheable(false)
@Table(name = "user_detail")
public class UserDetail {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "user_code")
    private String userCode;
    @Column(name = "user_name")
    private String userName;
    @Column(name = "installation_code")
    private String installationCode;
    @Column(name = "department_code")
    private String departmentCode;
    @Column(name = "region")
    private String region;
    @Column(name = "region_code")
    private String regionCode;
    @Column(name = "dated_nom_detailed_check")
    private String datedNomDetailedCheck;
    @Column(name = "nominated_acc_seq_number")
    private String nominatedAccSeqNumber;
    @Column(name = "account_number")
    private String accountNumber;
    @Column(name = "branch_code")
    private String branchCode;
    @Column(name = "user_reference")
    private String userReference;
    @Column(name = "account_status_code")
    private String accountStatusCode;
}