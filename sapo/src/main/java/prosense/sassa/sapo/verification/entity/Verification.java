package prosense.sassa.sapo.verification.entity;

import java.time.ZonedDateTime;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import prosense.sassa.sapo.beneficiaryapi.entity.Fingerprint;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = { "idNumber" })
@ToString
public class Verification {
    private String idNumber;
    private String fullname;
    private String surname;
    private String status;
    private String transaction;
    private String code;
    private String description;
    private Set<Fingerprint> fingerprints;
}
