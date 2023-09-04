package prosense.sassa.sapo.beneficiaryapi.entity;

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
public class Piva {
    private String id;
    private Fingerprint primary;
    private Fingerprint secondary;
    private String fullname;
    private String surname;
    private String status;
    private String transaction;
}
