package prosense.sassa.smshelper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import com.opencsv.bean.CsvBindByPosition;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(of = { "id" })
@ToString
public class Outcome {
    private Long id;
	@CsvBindByPosition(position = 0)
    private String idNumber;
	@CsvBindByPosition(position = 1)
    private String names;
	@CsvBindByPosition(position = 2)
    private String surname;
	@CsvBindByPosition(position = 3)
    private String mobile;
	@CsvBindByPosition(position = 4)
    private String party;
	@CsvBindByPosition(position = 5)
    private String application;
}
