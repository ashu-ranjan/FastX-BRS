package com.backend.fastx.dto;

import java.time.LocalDate;

import com.backend.fastx.enums.FeedbackStatus;

public class FeedbackSummaryDTO {
    private int id;
    private String comment;
    private LocalDate feedbackDate;
    private FeedbackStatus feedbackStatus;
    private int bookingId;
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public LocalDate getFeedbackDate() {
        return feedbackDate;
    }
    public void setFeedbackDate(LocalDate feedbackDate) {
        this.feedbackDate = feedbackDate;
    }
    public FeedbackStatus getFeedbackStatus() {
        return feedbackStatus;
    }
    public void setFeedbackStatus(FeedbackStatus feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }
    public int getBookingId() {
        return bookingId;
    }
    public void setBookingId(int bookingId) {
        this.bookingId = bookingId;
    }

    

}
