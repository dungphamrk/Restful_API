package ra.ss15.service;

import ra.ss15.model.dto.request.ReviewDTO;
import ra.ss15.model.dto.response.ReviewResponse;

import java.util.List;

public interface ReviewService {
    void createReview(ReviewDTO dto, Long userId);
    List<ReviewResponse> getReviewsByProductId(Long productId);
}
