package ra.ss10.entity.dto.response;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataResponse <T>{
    private T data;
    private String HttpStatus;
}
