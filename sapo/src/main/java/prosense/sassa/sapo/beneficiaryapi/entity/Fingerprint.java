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
@EqualsAndHashCode(of = { "id", "hand", "finger", "grade", "template"})
@ToString
public class Fingerprint {
    private Long id;
    private Long party;
    private String hand;
    private String finger;
    private String grade;
    private String template;
    private String image;
    private String imageType;
    private ZonedDateTime created;
    private String creator;
    private ZonedDateTime updated;
    private String updator;
}
