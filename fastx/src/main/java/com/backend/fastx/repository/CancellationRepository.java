package com.backend.fastx.repository;

import com.backend.fastx.model.Cancellation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CancellationRepository extends JpaRepository<Cancellation, Integer> {
}
