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
public class Verification {
    private Long id;
    private Long application;
    private Long party;
    private String status;
    private String transaction;
    private String fullname;
    private String surname;
    private String image;
    private String code;
    private String description;
    private ZonedDateTime created;
    private String creator;
    private ZonedDateTime updated;
    private String updator;
}
