package com.backend.fastx.repository;

import com.backend.fastx.model.BusOperator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;


public interface BusOperatorRepository extends JpaRepository<BusOperator, Integer> {

    Optional<BusOperator> findByUserUsername(String username);

    @Query("SELECT bo FROM BusOperator bo WHERE bo.user.username = ?1")
    Optional<BusOperator> getBusOperatorByUsername(String username);
}
