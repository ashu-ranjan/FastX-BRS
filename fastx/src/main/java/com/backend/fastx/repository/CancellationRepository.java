package com.backend.fastx.repository;

import com.backend.fastx.enums.RefundStatus;
import com.backend.fastx.model.Cancellation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CancellationRepository extends JpaRepository<Cancellation, Integer> {
    List<Cancellation> findByRefundStatus(RefundStatus refundStatus);
}
