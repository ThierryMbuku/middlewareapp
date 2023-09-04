package prosense.sassa.srdeft.transmissionfile.entity;

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

@Builder(toBuilder=true)
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = { "id" })
@ToString
@Entity
@Cacheable(false)
@Table(name = "transmission_file")
public class TransmissionFile {
    @SequenceGenerator(name = "transmission_file_seq_generator", sequenceName = "transmission_file_seq", allocationSize = 1)
    @Id
    @GeneratedValue(generator = "transmission_file_seq_generator", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "file_name")
    private String fileName;
    @Column(name = "service")
    private String service;
    @Column(name = "sub_service")
    private String subService;
    @Column(name = "type")
    private String type;
    @Column(name = "ack_status")
    private String ackStatus;
    @Column(name = "content")
    private String content;
    @Column(name = "error_code")
    private String errorCode;
    @Column(name = "test")
    private Boolean test;
    @Column(name = "creator")
    private String creator;
    @Column(name = "created")
    private ZonedDateTime created;

    public enum Service {
        eft
    }

    public enum SubService {
        same_day,
        one_day,
        two_day
    }

    public enum Type {
        ctl_begin,
        ctl_end,
        ack_begin,
        ack_end,
        ack_vet
    }

    public enum AckStatus {
        positive,
        negative
    }
}

