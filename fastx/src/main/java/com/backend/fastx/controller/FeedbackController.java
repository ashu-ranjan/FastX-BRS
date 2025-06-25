package com.backend.fastx.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import com.backend.fastx.dto.FeedbackDTO;
import com.backend.fastx.service.FeedbackService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;




@RestController
public class FeedbackController {

    @Autowired private FeedbackService feedbackService;

    @PostMapping("/fastx/api/feedback/submit")
    public ResponseEntity<?> postFeedback(@RequestBody FeedbackDTO feedbackDTO,
                                            Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(feedbackService.submitFeedback(feedbackDTO,username));
    }

    @GetMapping("/fastx/api/feedback/get/by-operator")
    public ResponseEntity<?> getOperatorFeedback(Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(feedbackService.getFeedbackForOperator(username));
    }

    @GetMapping("/fastx/api/feedback/bus/{busId}")
    public ResponseEntity<?> getFeedbackSummary(@PathVariable int busId) {
        return ResponseEntity.ok(feedbackService.getFeedbackSummaryForBus(busId));
    }
    
    
    
}
