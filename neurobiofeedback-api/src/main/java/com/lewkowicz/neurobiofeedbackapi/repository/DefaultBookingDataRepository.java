package com.lewkowicz.neurobiofeedbackapi.repository;

import com.lewkowicz.neurobiofeedbackapi.entity.DefaultBookingData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DefaultBookingDataRepository extends JpaRepository<DefaultBookingData, Long> {

    Optional<DefaultBookingData> findByEmail(String email);

}
