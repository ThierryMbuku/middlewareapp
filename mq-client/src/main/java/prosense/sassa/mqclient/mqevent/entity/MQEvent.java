package prosense.sassa.mqclient.mqevent.entity;

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
@EqualsAndHashCode(of = { "id" })
@ToString
@Entity
@Cacheable(false)
@Table(name = "mqevent")
public class MQEvent {
    @SequenceGenerator(name = "mqevent_seq_generator", sequenceName = "mqevent_seq", allocationSize = 1)
    @Id
    @GeneratedValue(generator = "mqevent_seq_generator", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "transaction")
    private Long transaction;
    @Column(name = "sequence")
    private Long sequence;
    @Column(name = "message_id")
    private String messageId;
    @Column(name = "correlation")
    private String correlation;
    @Column(name = "type")
    private String type;
    @Column(name = "effected_by")
    private String effectedBy;
    @Column(name = "effected_on")
    private String effectedOn;
    @Column(name = "occurred")
    private ZonedDateTime occurred;
    @Column(name = "context")
    private String context;
    @Column(name = "idDob")
    private String idDob;
    @Column(name = "creator")
    private String creator;
    @Column(name = "created")
    private ZonedDateTime created;

    public enum MQEventType {PUT, GET, POST}
}

