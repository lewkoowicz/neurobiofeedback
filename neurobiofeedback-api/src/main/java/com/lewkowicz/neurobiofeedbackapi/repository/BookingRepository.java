package com.lewkowicz.neurobiofeedbackapi.repository;

import com.lewkowicz.neurobiofeedbackapi.entity.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    List<Booking> findByEmail(String email);

    Optional<Booking> findByBookingDate(LocalDateTime bookingDate);

    List<Booking> findByBookingDateBetween(LocalDateTime start, LocalDateTime end);

}
