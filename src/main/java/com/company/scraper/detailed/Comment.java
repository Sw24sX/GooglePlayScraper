package com.company.scraper.detailed;

import java.util.Date;

public class Comment {
    public final String name;
    public final Integer score;
    public final Date date;
    public final String commentText;
    public final String answerText;

    public Comment(String name, Integer score, Date date, String commentText, String answerText) {
        this.name = name;
        this.score = score;
        this.date = date;
        this.commentText = commentText;
        this.answerText = answerText;
    }

    public Comment(String name, Integer score, Date date, String commentText) {
        this.name = name;
        this.score = score;
        this.date = date;
        this.commentText = commentText;
        this.answerText = null;
    }
}
