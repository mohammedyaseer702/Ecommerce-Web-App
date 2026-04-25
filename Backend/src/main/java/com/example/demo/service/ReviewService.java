package com.example.demo.service;

import com.example.demo.entity.Review;
import com.example.demo.entity.User;
import com.example.demo.entity.Product;
import com.example.demo.entity.ReviewVote;
import com.example.demo.enums.VoteType;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewVoteRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ReviewVoteRepository reviewVoteRepository;

    // ✅ ADD REVIEW
    public Review addReview(String email, Long productId, int rating, String comment) {

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Review review = new Review();
        review.setUser(user);
        review.setProductId(productId); // ✅ IMPORTANT FIX
        review.setRating(rating);
        review.setComment(comment);
        review.setCreatedAt(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    // ✅ GET REVIEWS
    public List<Review> getProductReviews(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    // ✅ LIKE / DISLIKE (REAL AMAZON LOGIC)
    public Review voteReview(Long reviewId, String email, VoteType voteType) {

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Optional<ReviewVote> existingVote =
                reviewVoteRepository.findByReviewIdAndUserId(reviewId, user.getId());

        if (existingVote.isPresent()) {

            ReviewVote vote = existingVote.get();

            // 🔁 TOGGLE LOGIC
            if (vote.getVoteType() == voteType) {
                reviewVoteRepository.delete(vote); // remove vote
            } else {
                vote.setVoteType(voteType); // switch like ↔ dislike
                reviewVoteRepository.save(vote);
            }

        } else {
            ReviewVote vote = new ReviewVote();
            vote.setReview(review);
            vote.setUser(user);
            vote.setVoteType(voteType);
            reviewVoteRepository.save(vote);
        }

        return review;
    }

    // ✅ COUNT LIKES / DISLIKES
    public Map<String, Long> getVotes(Long reviewId) {

        long likes = reviewVoteRepository.countByReviewIdAndVoteType(reviewId, VoteType.LIKE);
        long dislikes = reviewVoteRepository.countByReviewIdAndVoteType(reviewId, VoteType.DISLIKE);

        Map<String, Long> result = new HashMap<>();
        result.put("likes", likes);
        result.put("dislikes", dislikes);

        return result;
    }
}