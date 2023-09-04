package prosense.sassa.srdeft.payday.entity;

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
@EqualsAndHashCode
@ToString
public class PayDay {
    private ZonedDateTime processingDate;
    private String subService;
    private String period;
    private Long payDay;

    public enum SubService {
        same_day,
        one_day,
        two_day
    }
}

