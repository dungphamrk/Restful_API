package ra.ss7.model.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class DataResponse<T> {
    private T data;
    private HttpStatus status;
}
