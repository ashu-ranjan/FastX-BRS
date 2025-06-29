package com.backend.fastx.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.backend.fastx.model.Bus;
import com.backend.fastx.model.Driver;

public interface DriverRepository extends JpaRepository<Driver, Integer>{

    Optional<Driver> findByBus(Bus bus);


}
