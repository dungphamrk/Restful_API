package ra.ss14.model.dto.response;

import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDTO{
    private UUID id;
    private String username;
    private String email;
    private List<String> roles;
}