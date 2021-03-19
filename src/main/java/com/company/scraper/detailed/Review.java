package com.company.scraper.detailed;

import java.util.Date;

public class Review {
    public final String reviewId;
    public final String userName;
    public final String userImageSrc;
    public final Integer score;
    public final Date dateReview;
    public final String review;
    public String answer;
    public String developerName;
    public Date answerDate;
    public final Integer likes;
    public final String reviewCreatedVersion;

    public Review(String reviewId, String userName, String userImageSrc, String review,
                  Integer score, Date dateReview, Integer likes, String reviewCreatedVersion,
                  String answer, String developerName, Date answerDate) {
        this.reviewId = reviewId;
        this.userName = userName;
        this.userImageSrc = userImageSrc;
        this.review = review;
        this.score = score;
        this.dateReview = dateReview;
        this.likes = likes;
        this.reviewCreatedVersion = reviewCreatedVersion;
        this.answer = answer;
        this.developerName = developerName;
        this.answerDate = answerDate;
    }

    public Review(String reviewId, String userName, String userImageSrc, String review,
                  Integer score, Date dateReview, Integer likes, String reviewCreatedVersion) {
        this.reviewId = reviewId;
        this.userName = userName;
        this.userImageSrc = userImageSrc;
        this.review = review;
        this.score = score;
        this.dateReview = dateReview;
        this.likes = likes;
        this.reviewCreatedVersion = reviewCreatedVersion;
        this.answer = null;
        this.developerName = null;
        this.answerDate = null;
    }

    public void addAnswer(String answer, String developerName, Date answerDate) {
        this.answer = answer;
        this.developerName = developerName;
        this.answerDate = answerDate;
    }
}
