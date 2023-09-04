package prosense.sassa.srdeft.outcome.srd.entity;

import java.time.ZonedDateTime;

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
@Table(name = "outcome_staging")
public class Outcome {
    @Id
    @Column(name = "oid")
    private Long id;
    @Column(name = "period")
    private String period;
    @Column(name = "outcome")
    private String outcome;
    @Column(name = "payday")
    private Long payDay;
    @Column(name = "paymonth")
    private Long payMonth;
    @Column(name = "payyear")
    private Long payYear;
    @Column(name = "payseq")
    private Long paySeq;
    @Column(name = "province")
    private String province;
    @Column(name = "bank")
    private String bank;
    @Column(name = "branch")
    private String branch;
    @Column(name = "account")
    private String account;
    @Column(name = "type")
    private String type;
    @Column(name = "holder")
    private String holder;
    @Column(name = "processed")
    private ZonedDateTime processed;
}