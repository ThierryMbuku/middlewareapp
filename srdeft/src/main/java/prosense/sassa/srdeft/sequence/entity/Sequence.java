package prosense.sassa.srdeft.sequence.entity;

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
@Table(name = "sequences")
public class Sequence {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "region")
    private String region;
    @Column(name = "frequency")
    private String frequency;
    @Column(name = "type")
    private String type;
    @Column(name = "sub_service")
    private String subService;
    @Column(name = "min_value")
    private Long minValue;
    @Column(name = "max_value")
    private Long maxValue;
    @Column(name = "cur_value")
    private Long curValue;
    @Column(name = "creator")
    private String creator;
    @Column(name = "created")
    private ZonedDateTime created;
    @Column(name = "updator")
    private String updator;
    @Column(name = "updated")
    private ZonedDateTime updated;
}