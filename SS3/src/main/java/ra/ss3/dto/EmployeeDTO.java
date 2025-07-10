package ra.ss3.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EmployeeDTO {
    private Long id;
    private String name;
    private String email;
    private String phoneNumber;
    private double salary;
    private LocalDate createdAt;


}
