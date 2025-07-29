package ra.test_exam.model.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CustomerPrincipal implements UserDetails {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String phone;
}
