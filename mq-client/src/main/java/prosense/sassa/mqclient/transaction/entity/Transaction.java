package prosense.sassa.mqclient.transaction.entity;

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
public class Transaction {
    private Long id;
    private String correlation;
    private String domainUser;
    private String socpenUser;
    private String content;
    private String detail;
    private String type;
    private String challenge;
    private String hash;
    private String state;
    private String policy;
    private String status;
    private String cipher;
    private String choice;
    private String created;
    private String creator;
    private String updated;
    private String updator;
    private String[] messages;
}
