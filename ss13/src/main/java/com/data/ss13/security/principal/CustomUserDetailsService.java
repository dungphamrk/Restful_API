package com.data.ss13.security.principal;

import com.data.ss13.model.entity.Role;
import com.data.ss13.model.entity.User;
import com.data.ss13.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Khong ton tai username"));
        return CustomUserDetails.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .email(user.getEmail())
                .status(user.isStatus())
                .authorities(mapRoleToGrandAuthorities(user.getRoles()))
                .build();
    }

    private Set<? extends GrantedAuthority> mapRoleToGrandAuthorities(Set<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
    }
}