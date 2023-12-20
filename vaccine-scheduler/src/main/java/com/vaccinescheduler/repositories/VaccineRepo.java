package com.vaccinescheduler.repositories;

import com.vaccinescheduler.models.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VaccineRepo extends JpaRepository<Vaccine, Integer> {
    @Query("SELECT v FROM Vaccine v WHERE v.minAge >= 18")
    Optional<List<Vaccine>> getAdultVaccines();
    @Query("FROM Vaccine WHERE maxAge < 18")
    Optional<List<Vaccine>> getChildVaccines();
    Optional<List<Vaccine>> findByVaccineName(String vaccineName);
}
