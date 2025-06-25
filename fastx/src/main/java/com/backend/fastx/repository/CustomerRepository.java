package com.backend.fastx.repository;

import com.backend.fastx.model.Customer;
import com.backend.fastx.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Optional<Customer> findByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE c.user.username = ?1")
    Optional<Customer> getCustomerByUsername(String username);

    Optional<Customer> findByUser(User user);
}
