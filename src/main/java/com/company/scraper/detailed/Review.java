package com.company.scraper.detailed;

import java.time.LocalDateTime;
import java.util.Date;

public class Review {
    public final String reviewId;
    public final String userName;
    public final String userImageSrc;
    public final Integer score;
    public final LocalDateTime dateReview;
    public final String review;
    public String answer;
    public LocalDateTime answerDate;
    public final Integer likes;
    public final String reviewCreatedVersion;

    public Review(String reviewId, String userName, String userImageSrc, String review,
                  Integer score, LocalDateTime dateReview, Integer likes, String reviewCreatedVersion,
                  String answer, LocalDateTime answerDate) {
        this.reviewId = reviewId;
        this.userName = userName;
        this.userImageSrc = userImageSrc;
        this.review = review;
        this.score = score;
        this.dateReview = dateReview;
        this.likes = likes;
        this.reviewCreatedVersion = reviewCreatedVersion;
        this.answer = answer;
        this.answerDate = answerDate;
    }

    public Review(String reviewId, String userName, String userImageSrc, String review,
                  Integer score, LocalDateTime dateReview, Integer likes, String reviewCreatedVersion) {
        this.reviewId = reviewId;
        this.userName = userName;
        this.userImageSrc = userImageSrc;
        this.review = review;
        this.score = score;
        this.dateReview = dateReview;
        this.likes = likes;
        this.reviewCreatedVersion = reviewCreatedVersion;
        this.answer = null;
        this.answerDate = null;
    }

    public void addAnswer(String answer, LocalDateTime answerDate) {
        this.answer = answer;
        this.answerDate = answerDate;
    }
}
