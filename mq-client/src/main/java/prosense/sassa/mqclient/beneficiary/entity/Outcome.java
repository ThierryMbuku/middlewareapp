package prosense.sassa.mqclient.beneficiary.entity;

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
public class Outcome {
    private Long id;
    private Long application;
    private String idNumber;
    private String socpenUser;
    private String office;
    private String socpenTime;
    private String method;
    private String ref;
    private String type1;
    private String type2;
    private String type3;
    private String type4;
    private String status1;
    private String status2;
    private String status3;
    private String status4;
    private String reason1;
    private String reason2;
    private String reason3;
    private String reason4;
    private String messageId;
    private String rawMessage;
    private String created;
    private String creator;
    private String updated;
    private String updator;
}
