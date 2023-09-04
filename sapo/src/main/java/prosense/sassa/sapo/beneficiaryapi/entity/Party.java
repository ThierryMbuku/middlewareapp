package prosense.sassa.sapo.beneficiaryapi.entity;

import java.time.ZonedDateTime;

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
public class Party {
    private Long id;
    private Long party;
    private String idNumber;
    private String fullname;
    private String surname;
    private Boolean fingerprinted;
    private ZonedDateTime created;
    private String creator;
    private ZonedDateTime updated;
    private String updator;
}
