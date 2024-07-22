package com.lewkowicz.neurobiofeedbackapi.repository;

import com.lewkowicz.neurobiofeedbackapi.entity.WorkingTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkingTimeRepository extends JpaRepository<WorkingTime, Long> { }
