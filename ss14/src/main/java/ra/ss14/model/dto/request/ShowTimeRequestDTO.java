package ra.ss14.model.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ShowTimeRequestDTO{
    @NotNull(message = "MovieId must not be null")
    private Long movieId;

    @NotBlank(message = "Start time cannot be empty")
    @NotBlank(message = "Start time cannot be empty")
    @Pattern(
            regexp = "^\\d{4}-\\d{2}-\\d{2} \\d{2}:\\d{2}$",
            message = "Start time must be in format yyyy-MM-dd HH:mm"
    )
    private String startTime;

    @NotBlank(message = "Room name cannot be empty")
    @Pattern(regexp = "^$|^[0-9]{3}$", message = "Room name must be 3 digits")
    private String room;
}
