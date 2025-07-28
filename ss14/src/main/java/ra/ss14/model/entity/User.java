package ra.ss14.model.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

import java.util.UUID;

@Entity
@Table(name = "users")
@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Builder
public class User{
    @Id
    @GeneratedValue
    private UUID id;
    @Column(unique = true, nullable = false, length = 100)
    private String username;
    @Column(nullable = false, length = 100)
    private String password;
    @Column(unique = true, nullable = false, length = 100)
    private String email;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Role> roles;

    private String otp;
}
