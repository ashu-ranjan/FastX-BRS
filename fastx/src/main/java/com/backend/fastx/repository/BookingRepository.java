package com.backend.fastx.repository;

import com.backend.fastx.model.Booking;
import com.backend.fastx.model.Bus;
import com.backend.fastx.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
    List<Booking> findByCustomer(Customer customer);

    @Query("SELECT b FROM Booking b WHERE b.id = ?1 AND b.customer.email = ?2")
    Optional<Booking> findByIdAndCustomerEmail(int id, String username);

    List<Booking> findByScheduleBusIn(List<Bus> buses);
}
