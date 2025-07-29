package ra.ss15.controller;

import ra.ss15.model.dto.request.ReviewDTO;
import ra.ss15.model.dto.response.ReviewResponse;
import ra.ss15.secutiry.principal.CustomUserDetailsService;
import ra.ss15.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<?> createReview(@RequestBody @Valid ReviewDTO dto) {
        reviewService.createReview(dto, CustomUserDetailsService.getCurrentUserId());
        return ResponseEntity.ok("Đánh giá thành công");
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<List<ReviewResponse>> getProductReviews(@PathVariable Long id) {
        return ResponseEntity.ok(reviewService.getReviewsByProductId(id));
    }
}