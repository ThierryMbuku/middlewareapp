package prosense.sassa.srdeft.batchcovjob.entity;

import java.time.ZonedDateTime;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import prosense.sassa.srdeft.covjob.entity.CovJob;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = { "id" })
@ToString
@Entity
@Cacheable(false)
@Table(name = "batch_cov_job")
public class BatchCovJob {
    @SequenceGenerator(name = "batch_cov_job_seq_generator", sequenceName = "batch_cov_job_seq", allocationSize = 1)
    @Id
    @GeneratedValue(generator = "batch_cov_job_seq_generator", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "processing_date")
    private ZonedDateTime processingDate;
    @Column(name = "verification_period")
    private String verificationPeriod;
    @Column(name = "period")
    private String period;
    @Column(name = "pay_day")
    private Long payDay;
    @Column(name = "sub_service")
    private String subService;
    @Column(name = "type")
    private String type;
    @Column(name = "by_region")
    private Boolean byRegion;
    @Column(name = "test")
    private Boolean test;
    @Column(name = "status")
    private String status;
    @Column(name = "sign_status")
    private String signStatus;
    @Column(name = "transactions_per_job")
    private Long transactionsPerJob;
    @Column(name = "transactions_total")
    private Long transactionsTotal;
    @Column(name = "transactions_processed")
    private Long transactionsProcessed;
    @Column(name = "transactions_rejected")
    private Long transactionsRejected;
    @Column(name = "jstart")
    private ZonedDateTime start;
    @Column(name = "jend")
    private ZonedDateTime end;
    @Column(name = "messages")
    private String messages;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "batch_cov_job", referencedColumnName = "id", insertable = false, updatable = false)
    private Set<CovJob> covJobs;
    @Column(name = "creator")
    private String creator;
    @Column(name = "created")
    private ZonedDateTime created;
    @Column(name = "updator")
    private String updator;
    @Column(name = "updated")
    private ZonedDateTime updated;

    public enum VerificationPeriod {
        MAY2020,
        JUN2020,
        JUL2020,
        AUG2020,
        SEP2020,
        OCT2020
    }

    public enum Period {
        MAY2020,
        JUN2020,
        JUL2020,
        AUG2020,
        SEP2020,
        OCT2020
    }

    public enum SubService {
        same_day,
        one_day,
        two_day
    }

    public enum Type {
        eft
    }
    
    public enum Status {
        pending,
        cancelled,
        started,
        completed,
        partially_completed,
        failed
    }

    public enum SignStatus {
        approved,
        declined,
        unknown,
        expired,
        locked
    }
}

