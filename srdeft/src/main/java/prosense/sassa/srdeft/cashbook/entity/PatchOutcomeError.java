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
@Table(name = "patch_outcome_error")
public class PatchOutcomeError {
    @SequenceGenerator(name = "patch_outcome_error_seq_generator", sequenceName = "patch_outcome_error_seq", allocationSize = 1)
    @Id
    @GeneratedValue(generator = "patch_outcome_error_seq_generator", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "data_file")
    private Long dataFile;
    @Column(name = "outcome")
    private Long outcome;
    @Column(name = "type")
    private String type;
    @Column(name = "error")
    private String error;
    @Column(name = "creator")
    private String creator;
    @Column(name = "created")
    private ZonedDateTime created;
}

