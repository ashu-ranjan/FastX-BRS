package com.backend.fastx.controller;

import com.backend.fastx.dto.CancelBookingRequestDTO;
import com.backend.fastx.dto.CancellationApprovalDTO;
import com.backend.fastx.service.CancellationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CancellationController {

    @Autowired private CancellationService cancellationService;

    @PostMapping("/fastx/api/cancellation/request")
    public ResponseEntity<?> cancelPassenger(@RequestParam int bookingDetailId,
                                             @RequestBody CancelBookingRequestDTO requestDTO,
                                             Principal principal){
        String email = principal.getName();
        cancellationService.createCancellationRequest(bookingDetailId, requestDTO, email);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cancellation request submitted and pending approval.");
        return ResponseEntity.ok(response);

    }
    @PutMapping("/fastx/api/cancellation/approval")
    public ResponseEntity<?> approveCancellation(@RequestBody CancellationApprovalDTO dto,
                                                      @AuthenticationPrincipal UserDetails userDetails) {
        cancellationService.approveCancellation(dto, userDetails.getUsername());
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cancellation processed successfully.");
        return ResponseEntity.ok(response);
    }
}
