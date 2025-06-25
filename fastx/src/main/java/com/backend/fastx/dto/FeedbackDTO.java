package com.backend.fastx.dto;

import com.backend.fastx.enums.FeedbackStatus;

public class FeedbackDTO {
    private int booingId;
    private String comment;
    private FeedbackStatus feedbackStatus;
    
    public int getBooingId() {
        return booingId;
    }
    public void setBooingId(int booingId) {
        this.booingId = booingId;
    }
    public String getComment() {
        return comment;
    }
    public void setComment(String comment) {
        this.comment = comment;
    }
    public FeedbackStatus getFeedbackStatus() {
        return feedbackStatus;
    }
    public void setFeedbackStatus(FeedbackStatus feedbackStatus) {
        this.feedbackStatus = feedbackStatus;
    }

    
}
