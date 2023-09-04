package prosense.sassa.srdeft.cashbook.entity;

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
import javax.persistence.CascadeType;
import javax.persistence.JoinColumn;
import javax.persistence.PostPersist;

import java.util.Iterator;

import java.util.Set;

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
@EqualsAndHashCode
@ToString
@Entity
@Cacheable(false)
@Table(name = "data_file")
public class DataFile {
    @SequenceGenerator(name = "data_file_seq_generator", sequenceName = "data_file_seq", allocationSize = 1)
    @Id
    @GeneratedValue(generator = "data_file_seq_generator", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "cash_book")
    private Long cashBook;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "file_number")
    private String fileNumber;
    @Column(name = "installation_generation_number")
    private String installationGenerationNumber;
    @Column(name = "user_generation_number")
    private String userGenerationNumber;
    @Column(name = "serial_number")
    private String serialNumber;
    @Column(name = "number_of_records")
    private String numberOfRecords;
    @Column(name = "record_count")
    private String recordCount;
    @Column(name = "first_sequence_number")
    private String firstSequenceNumber;
    @Column(name = "last_sequence_number")
    private String lastSequenceNumber;
    @Column(name = "total_debit_value")
    private String totalDebitValue;
    @Column(name = "total_credit_value")
    private String totalCreditValue;
    @Column(name = "user_code")
    private String userCode;
    @Column(name = "user_branch")
    private String userBranch;
    @Column(name = "user_nominated_account_number")
    private String userNominatedAccountNumber;
    @Column(name = "action_date")
    private String actionDate;
    @Column(name = "test_live_indicator")
    private String testLiveIndicator;
    @Column(name = "settlement_date")
    private String settlementDate;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "data_file", referencedColumnName = "id", insertable = true)
    private Set<PatchOutcomeError> patchOutcomeErrors;
    @Column(name = "creator")
    private String creator;
    @Column(name = "created")
    private ZonedDateTime created;
    
    @PostPersist
    public void initialize() {
        if (patchOutcomeErrors.size() != 0) {
            Iterator<PatchOutcomeError> iterator = patchOutcomeErrors.iterator();
            while (iterator.hasNext())
                iterator.next().setDataFile(id);
        }
    }
}

