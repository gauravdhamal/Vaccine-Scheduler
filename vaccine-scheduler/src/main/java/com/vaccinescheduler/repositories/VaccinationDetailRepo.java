package com.vaccinescheduler.repositories;

import com.vaccinescheduler.models.VaccinationDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface VaccinationDetailRepo extends JpaRepository<VaccinationDetail, Integer> {
    Optional<List<VaccinationDetail>> findByVaccinatedDateAndVaccinatedTime(LocalDate vaccinatedDate, LocalTime vaccinatedTime);
}
