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
@EqualsAndHashCode(of = { "quadNumber" })
@ToString
public class Quad {
    private String quadNumber;
    private String fullname;
    private String surname;
    private String created;
    private String creator;
    private String updated;
    private String updator;
}
