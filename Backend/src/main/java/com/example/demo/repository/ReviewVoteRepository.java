package com.example.demo.repository;

import com.example.demo.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import com.example.demo.enums.VoteType;

public interface ReviewVoteRepository extends JpaRepository<ReviewVote, Long> {

    Optional<ReviewVote> findByReviewIdAndUserId(Long reviewId, Long userId);

    long countByReviewIdAndVoteType(Long reviewId, VoteType voteType);
}