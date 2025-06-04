package com.backend.fastx.repository;

import com.backend.fastx.model.BusOperator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface BusOperatorRepository extends JpaRepository<BusOperator, Integer> {

    Optional<BusOperator> findByUserUsername(String username);
}
