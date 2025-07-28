package ra.ss14.model.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class MovieRequestDTO{
    @NotBlank(message = "Title cannot be empty")
    private String title;
    @NotBlank(message = "Description cannot be empty")
    private String description;
    @Min(value = 1, message = "Duration must be greater than 0")
    private int duration;

    @NotBlank(message = "Release date cannot be empty")
    @Pattern(regexp = "^$|^\\d{4}-\\d{2}-\\d{2}$", message = "Release date must be in format yyyy-MM-dd")
    private String releaseDate;
}