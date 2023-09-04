package prosense.sassa.srdclient.sarscompliance.entity;

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
@EqualsAndHashCode(of = { "outcomeId" })
@ToString
public class SarsCompliance {
    private Long outcomeId;
    private String identificationType;
    private String identificationNumber;
    private String countryOfIssue;
    private String[] messages;

    public enum IdentificationType {
        south_african_id_number,
        foreign_passport_number,
        asylum_permit_number
    }
}
