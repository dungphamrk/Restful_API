package ra.ss10.entity.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Account {
    @Id
    private UUID id;
    private String fullName;
    private String email;
    private String password;
    private String cccd;
    private String phone;
    private Double money;
    private Status status;

}
