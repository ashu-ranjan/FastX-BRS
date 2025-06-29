package com.backend.fastx.repository;

import com.backend.fastx.model.Executive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ExecutiveRepository extends JpaRepository<Executive, Integer> {
    @Query("SELECT e FROM Executive e WHERE e.user.username = ?1")
    Optional<Executive> getExecutiveByUsername(String username);

    Optional<Executive> findByEmail(String email);
}
