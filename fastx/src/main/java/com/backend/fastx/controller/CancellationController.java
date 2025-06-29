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
@CrossOrigin(origins = "http://localhost:5173")
public class CancellationController {

    @Autowired private CancellationService cancellationService;

    /**
     * @aim Cancel a passenger's booking
     * @description This method allows a passenger to request cancellation of their booking.
     *              It requires the booking detail ID and cancellation request details in the request body.
     * @path /fastx/api/cancellation/request/{bookingDetailId}
     * @method POST
     * @param bookingDetailId The ID of the booking detail to cancel.
     * @param requestDTO The cancellation request details.
     * @param principal The authenticated user's principal.
     * @return ResponseEntity with a message indicating the cancellation request status.
     */

    @PostMapping("/request/{bookingDetailId}")
    public ResponseEntity<?> cancelPassenger(@PathVariable int bookingDetailId,
                                             @RequestBody CancelBookingRequestDTO requestDTO,
                                             Principal principal){
        String email = principal.getName();
        cancellationService.createCancellationRequest(bookingDetailId, requestDTO, email);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cancellation request submitted and pending approval.");
        return ResponseEntity.ok(response);

    }

    /**
     * @aim Approve or reject a cancellation request
     * @description This method allows an executive to approve or reject a cancellation request.
     *              It requires the cancellation approval details in the request body.
     * @path /fastx/api/cancellation/approval
     * @method PUT
     * @param dto The cancellation approval details containing the request ID and approval status.
     * @param principal The authenticated user's principal.
     * @return ResponseEntity with a message indicating the cancellation processing status.
     */

    @PutMapping("/approval")
    public ResponseEntity<?> approveCancellation(@RequestBody CancellationApprovalDTO dto,
                                                 Principal principal) {
        String username = principal.getName();
        cancellationService.approveCancellation(dto, username);
        Map<String, String> response = new HashMap<>();
        response.put("message", "Cancellation processed successfully.");
        return ResponseEntity.ok(response);
    }

    /**
     * @aim Get all pending cancellation requests
     * @description This method retrieves all pending cancellation requests.
     *              It returns a list of pending cancellations.
     * @path /fastx/api/cancellation/get-all/pending
     * @method GET
     * @return ResponseEntity with a list of pending cancellations.
     */

    @GetMapping("/get-all/pending")
    public ResponseEntity<?> getPendingCancellations(){
        return ResponseEntity.ok(cancellationService.getRequestedCancellation());
    }

    /**
     * @aim Get all cancellation history
     * @description This method retrieves the history of all cancellations.
     *              It returns a list of all cancellation records.
     * @path /fastx/api/cancellation/history
     * @method GET
     * @return ResponseEntity with a list of all cancellation records.
     */

    @GetMapping("/history")
    public ResponseEntity<?> getAllCancellation(){
        return ResponseEntity.ok(cancellationService.getCancellationHistory());
    }

   
}
