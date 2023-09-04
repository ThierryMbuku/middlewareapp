package prosense.sassa.srdeft.publicholiday.entity;

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
@EqualsAndHashCode(of = { "date" })
@ToString
@Entity
@Cacheable(false)
@Table(name = "public_holiday")
public class PublicHoliday {
    @SequenceGenerator(name = "public_holiday_seq_generator", sequenceName = "public_holiday_seq", allocationSize = 1)
    @Id
    @GeneratedValue(generator = "public_holiday_seq_generator", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "ph_date")
    private ZonedDateTime date;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "creator")
    private String creator;
    @Column(name = "created")
    private ZonedDateTime created;
    @Column(name = "updator")
    private String updator;
    @Column(name = "updated")
    private ZonedDateTime updated;
}

