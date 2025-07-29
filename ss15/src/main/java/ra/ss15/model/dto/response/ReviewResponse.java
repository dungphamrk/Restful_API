package ra.ss15.model.dto.response;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ReviewResponse {
    private String username;
    private int rating;
    private String comment;
    private LocalDateTime createdDate;
}