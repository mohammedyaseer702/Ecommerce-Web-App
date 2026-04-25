package com.example.demo.controller;
import com.example.demo.dto.ReviewRequestDTO;
import com.example.demo.entity.Review;
import com.example.demo.enums.VoteType;
import com.example.demo.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin
public class ReviewController {

    private final ReviewService reviewService;

   @PostMapping
public Review addReview(@RequestBody ReviewRequestDTO request,
                        Principal principal) {

    if (principal == null) {
        throw new RuntimeException("User not logged in");
    }

    return reviewService.addReview(
            principal.getName(),
            request.getProductId(),
            request.getRating(),
            request.getComment()
    );
}

@GetMapping("/{productId}")
public List<Review> getReviews(@PathVariable Long productId) {
    try {
        return reviewService.getProductReviews(productId);
    } catch (Exception e) {
        e.printStackTrace(); // 🔥 see real error
        throw e;
    }
}

@GetMapping("/{id}/votes")
public Map<String, Long> getVotes(@PathVariable Long id) {
    return reviewService.getVotes(id);
}

@PutMapping("/{id}/like")
public Review likeReview(@PathVariable Long id, Principal principal) {
    return reviewService.voteReview(id, principal.getName(), VoteType.LIKE);
}

@PutMapping("/{id}/dislike")
public Review dislikeReview(@PathVariable Long id, Principal principal) {
    return reviewService.voteReview(id, principal.getName(), VoteType.DISLIKE);
}
}
