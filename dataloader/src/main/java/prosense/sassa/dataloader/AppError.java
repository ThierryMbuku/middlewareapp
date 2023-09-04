package prosense.sassa.dataloader;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Singular;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppError {
    @Singular
    public List<String> messages;
}
