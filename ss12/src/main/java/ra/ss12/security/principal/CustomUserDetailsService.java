package ra.ss12.security.principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Primary
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;

    private final Map<String, UserDetails> users = new HashMap<>();

    {
        users.put("admin", User.builder()
                .username("admin")
                .password(passwordEncoder.encode("11111"))
                .roles("ADMIN")
                .build());

        users.put("user", User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (!users.containsKey(username)) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        return users.get(username);
    }
}
