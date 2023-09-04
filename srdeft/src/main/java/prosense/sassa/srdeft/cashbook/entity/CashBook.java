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
@EqualsAndHashCode(of = { "id" })
@ToString
@Entity
@Cacheable(false)
@Table(name = "cash_book")
public class CashBook {
    @SequenceGenerator(name = "cash_book_seq_generator", sequenceName = "cash_book_seq", allocationSize = 1)
    @Id
    @GeneratedValue(generator = "cash_book_seq_generator", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "cov_job")
    private Long covJob;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "serial_number")
    private String serialNumber;
    @Column(name = "department_code")
    private String departmentCode;
    @Column(name = "issue_date")
    private String issueDate;
    @Column(name = "payment_amount")
    private String paymentAmount;
    @Column(name = "payment_description")
    private String paymentDescription;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "cash_book", referencedColumnName = "id", insertable = true)
    private Set<DataFile> dataFiles;
    @Column(name = "creator")
    private String creator;
    @Column(name = "created")
    private ZonedDateTime created;
    
    @PostPersist
    public void initialize() {
        if (dataFiles.size() != 0) {
            Iterator<DataFile> iterator = dataFiles.iterator();
            while (iterator.hasNext())
                iterator.next().setCashBook(id);
        }
    }
}

