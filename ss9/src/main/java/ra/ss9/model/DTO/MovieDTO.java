package ra.ss9.model.DTO;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MovieDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDate releaseDate;
    private MultipartFile posterFile;
    private String poster;

}

