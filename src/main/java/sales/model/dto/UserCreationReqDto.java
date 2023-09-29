package sales.model.dto;

import jakarta.annotation.Nonnull;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Nonnull
public class UserCreationReqDto {
    private String name;
    private String surname;
    private String username;
    private String password;
}
