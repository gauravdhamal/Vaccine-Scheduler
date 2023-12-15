package com.vaccinescheduler.repositories;

import com.vaccinescheduler.models.Hospital;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HospitalRepo extends JpaRepository<Hospital, Integer> {
}
