package ra.ss12.security.config;

import ra.ss12.model.entity.User;
import ra.ss12.repository.UserRepo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class CustomAuthFilter extends OncePerRequestFilter{

    private UserRepo userRepo;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException{
        HttpSession session = request.getSession(false);
        if (session != null && session.getAttribute("username") != null) {
            String username = (String) session.getAttribute("username");
            User user = userRepo.findByUsername(username).orElse(null);
            if (user != null) {
                List<GrantedAuthority> authorities = user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getRoleName()))
                        .collect(Collectors.toList());

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        }
        chain.doFilter(request, response);
    }
}