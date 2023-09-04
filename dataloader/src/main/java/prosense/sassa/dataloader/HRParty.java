package prosense.sassa.dataloader;

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
@EqualsAndHashCode(of = { "nationalIdentifier" })
@ToString
public class HRParty {
    private String nationalIdentifier;
    private String firstName;
    private String lastName;
    private String email;
    private String mobile;
    private String region;
    private String empType;
    private String login;
    private String udfPosition;
}
