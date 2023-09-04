package prosense.sassa.mqclient.beneficiary.entity;

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
@Table(name = "outcome_error")
public class OutcomeError {
    @SequenceGenerator(name = "outcome_error_seq_generator", sequenceName = "outcome_error_seq", allocationSize = 1)
    @Id
    @GeneratedValue(generator = "outcome_error_seq_generator", strategy = GenerationType.SEQUENCE)
    @Column(name = "id")
    private Long id;
    @Column(name = "application")
    private Long application;
    @Column(name = "id_number")
    private String idNumber;
    @Column(name = "socpen_user")
    private String socpenUser;
    @Column(name = "office")
    private String office;
    @Column(name = "socpen_time")
    private String socpenTime;
    @Column(name = "method")
    private String method;
    @Column(name = "ref")
    private String ref;
    @Column(name = "type1")
    private String type1;
    @Column(name = "type2")
    private String type2;
    @Column(name = "type3")
    private String type3;
    @Column(name = "type4")
    private String type4;
    @Column(name = "status1")
    private String status1;
    @Column(name = "status2")
    private String status2;
    @Column(name = "status3")
    private String status3;
    @Column(name = "status4")
    private String status4;
    @Column(name = "reason1")
    private String reason1;
    @Column(name = "reason2")
    private String reason2;
    @Column(name = "reason3")
    private String reason3;
    @Column(name = "reason4")
    private String reason4;
    @Column(name = "message_id")
    private String messageId;
    @Column(name = "error_code")
    private String errorCode;
    @Column(name = "error_message")
    private String errorMessage;
    @Column(name = "raw_message")
    private String rawMessage;
    @Column(name = "api")
    private String api;
    @Column(name = "message_status")
    private String messageStatus;
    @Column(name = "creator")
    private String creator;
    @Column(name = "created")
    private ZonedDateTime created;
    @Column(name = "updator")
    private String updator;
    @Column(name = "updated")
    private ZonedDateTime updated;
}
