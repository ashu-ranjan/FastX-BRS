package com.backend.fastx.dto;

import java.time.LocalDate;

import com.backend.fastx.enums.FeedbackStatus;

public class FeedbackCommentDTO {
    private String customerName;
    private FeedbackStatus status;
    private String comment;
    private LocalDate feedbackDate;

    public FeedbackCommentDTO(String customerName, String comment, FeedbackStatus status, LocalDate feedbackDate) {
        this.customerName = customerName;
        this.status = status;
        this.comment = comment;
        this.feedbackDate = feedbackDate;
    }
    public String getCustomerName() {
        return customerName;
    }
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    public FeedbackStatus getStatus() {
        return status;
    }
    public void setStatus(FeedbackStatus status) {
        this.status = status;
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

    

}
