package com.vaccinescheduler.repositories;

import com.vaccinescheduler.models.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VaccineRepo extends JpaRepository<Vaccine, Integer> {
}
