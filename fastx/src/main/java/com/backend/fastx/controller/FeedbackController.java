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

    /**
     * @aim Submits feedback for a bus.
     * @description This method allows an operator to submit feedback for a bus.
     * @path /fastx/api/feedback/submit
     * @method POST
     * @param feedbackDTO The feedback data transfer object containing feedback details.
     * @param principal   The principal object representing the authenticated user.
     * @return ResponseEntity containing the result of the feedback submission.
     */

    @PostMapping("/fastx/api/feedback/submit")
    public ResponseEntity<?> postFeedback(@RequestBody FeedbackDTO feedbackDTO,
                                            Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(feedbackService.submitFeedback(feedbackDTO,username));
    }

    /**
     * @aim Retrieves feedback submitted by the operator.
     * @description This method allows an operator to retrieve feedback they have submitted.
     * @path /fastx/api/feedback/get/by-operator
     * @method GET
     * @param principal
     * @return ResponseEntity containing the feedback submitted by the operator.
     */
    @GetMapping("/fastx/api/feedback/get/by-operator")
    public ResponseEntity<?> getOperatorFeedback(Principal principal) {
        String username = principal.getName();
        return ResponseEntity.ok(feedbackService.getFeedbackForOperator(username));
    }

    /**
     * @aim Retrieves feedback summary for a specific bus.
     * @description This method allows an operator to retrieve the feedback summary for a specific bus.
     * @path /fastx/api/feedback/bus/{busId}
     * @method GET
     * @param busId The ID of the bus for which feedback summary is requested.
     * @return ResponseEntity containing the feedback summary for the specified bus.
     */

    @GetMapping("/fastx/api/feedback/bus/{busId}")
    public ResponseEntity<?> getFeedbackSummary(@PathVariable int busId) {
        return ResponseEntity.ok(feedbackService.getFeedbackSummaryForBus(busId));
    }
    
    
    
}
