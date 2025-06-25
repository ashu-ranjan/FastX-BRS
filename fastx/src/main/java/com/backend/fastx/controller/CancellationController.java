package com.backend.fastx.controller;

import com.backend.fastx.dto.CancelBookingRequestDTO;
import com.backend.fastx.dto.CancellationApprovalDTO;
import com.backend.fastx.service.CancellationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/fastx/api/cancellation")
public class CancellationController {

    @Autowired private CancellationService cancellationService;

    @PostMapping("/request")
    public ResponseEntity<?> cancelPassenger(@RequestParam int bookingDetailId,
                                             @RequestBody CancelBookingRequestDTO requestDTO,
                                             Principal principal){
        String email = principal.getName();
        cancellationService.createCancellationRequest(bookingDetailId, requestDTO, email);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cancellation request submitted and pending approval.");
        return ResponseEntity.ok(response);

    }
    @PutMapping("/approval")
    public ResponseEntity<?> approveCancellation(@RequestBody CancellationApprovalDTO dto,
                                                 Principal principal) {
        String username = principal.getName();
        cancellationService.approveCancellation(dto, username);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cancellation processed successfully.");
        return ResponseEntity.ok(response);
    }

    // Get all requested (PENDING) cancellations
    @GetMapping("/get-all/pending")
    public ResponseEntity<?> getPendingCancellations(){
        return ResponseEntity.ok(cancellationService.getRequestedCancellation());
    }

    @GetMapping("/history")
    public ResponseEntity<?> getAllCancellation(){
        return ResponseEntity.ok(cancellationService.getCancellationHistory());
    }
}
