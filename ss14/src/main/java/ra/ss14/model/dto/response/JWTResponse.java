package ra.ss14.model.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JWTResponse {
    private String username;
    private String email;
    private Boolean status;
    private Collection<? extends GrantedAuthority> authorities;
    private String token;
    private String refreshToken;

    public JWTResponse(String accessToken, String token){
        this.token = accessToken;
        this.refreshToken = token;
    }
}