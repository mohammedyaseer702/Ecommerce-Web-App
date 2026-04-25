package com.example.demo.entity;
import com.example.demo.enums.VoteType;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review_votes",
       uniqueConstraints = @UniqueConstraint(columnNames = {"review_id", "user_id"}))
public class ReviewVote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Review review;

    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private VoteType voteType;
}