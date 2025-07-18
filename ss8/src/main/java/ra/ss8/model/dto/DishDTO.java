package ra.ss8.model.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DishDTO {
    private String name;
    private String description;
    private Double price;
    private String status;
    private MultipartFile image;

}