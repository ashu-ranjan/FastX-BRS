package com.backend.fastx.dto;

import com.backend.fastx.enums.RefundStatus;

public class CancellationApprovalDTO {
    private int cancellationId;
    private RefundStatus refundStatus; // Only APPROVED or REJECTED
    private String remarks;

    public CancellationApprovalDTO() {
        // Default constructor
    }

    public CancellationApprovalDTO(int cancellationId, RefundStatus refundStatus, String remarks) {
        this.cancellationId = cancellationId;
        this.refundStatus = refundStatus;
        this.remarks = remarks;
    }

    public int getCancellationId() {
        return cancellationId;
    }

    public void setCancellationId(int cancellationId) {
        this.cancellationId = cancellationId;
    }

    public RefundStatus getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(RefundStatus refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}
