package com.backend.fastx.dto;

import java.util.List;

public class FeedbackCommentSummaryDTO {

    private double avgRating;
    private List<FeedbackCommentDTO> feedbackComments;
    public FeedbackCommentSummaryDTO(double avgRating, List<FeedbackCommentDTO> feedbackComments) {
        this.avgRating = avgRating;
        this.feedbackComments = feedbackComments;
    }
    public double getAvgRating() {
        return avgRating;
    }
    public void setAvgRating(double avgRating) {
        this.avgRating = avgRating;
    }
    public List<FeedbackCommentDTO> getFeedbackComments() {
        return feedbackComments;
    }
    public void setFeedbackComments(List<FeedbackCommentDTO> feedbackComments) {
        this.feedbackComments = feedbackComments;
    }

    
}
