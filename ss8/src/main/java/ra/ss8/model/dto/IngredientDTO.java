package ra.ss8.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class IngredientDTO {
    private String name;
    private Integer stock;
    private LocalDate expiry;
    private MultipartFile image;

}