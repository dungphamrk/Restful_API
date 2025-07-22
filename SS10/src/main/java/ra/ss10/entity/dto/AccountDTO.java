package ra.ss10.entity.dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ra.ss10.entity.model.Status;
import java.util.UUID;
import jakarta.validation.constraints.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountDTO {
    @NotNull(message = "ID cannot be null")
    private UUID id;

    @NotBlank(message = "Full name cannot be blank")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    @Pattern(regexp = "^[a-zA-Z\\s]*$", message = "Full name can only contain letters and spaces")
    private String fullName;

    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email must be a valid email address")
    @Size(max = 255, message = "Email must not exceed 255 characters")
    private String email;

    @NotBlank(message = "Password cannot be blank")
    @Size(min = 8, max = 128, message = "Password must be between 8 and 128 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).*$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, one special character, and no whitespace")
    private String password;

    @NotBlank(message = "CCCD cannot be blank")
    @Pattern(regexp = "^\\d{12}$", message = "CCCD must be exactly 12 digits")
    private String cccd;

    @NotNull(message = "Money cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Money must be greater than or equal to 0")
    private Double money;

    @NotNull(message = "Status cannot be null")
    private Status status;
}