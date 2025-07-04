package com.backend.fastx.repository;

import com.backend.fastx.model.Bus;
import com.backend.fastx.model.BusOperator;
import com.backend.fastx.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BusRepository extends JpaRepository<Bus, Integer> {
    @Query("SELECT b FROM Bus b JOIN FETCH b.busOperator WHERE b.id = ?1")
    Optional<Bus> findByIdWithOperator(int id);

    @Query("select b from Bus b where b.busOperator.user.username = ?1")
    List<Bus> findByOperator(User user);

    List<Bus> findByBusOperator(BusOperator operator);


}
