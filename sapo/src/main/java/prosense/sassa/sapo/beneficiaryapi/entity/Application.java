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
public class Application {
    private Long id;
    private Long party;
    private Long procurator;
    private String branch;
    private String status;
    private String mac;
    private ZonedDateTime created;
    private String creator;
    private ZonedDateTime updated;
    private String updator;
}
