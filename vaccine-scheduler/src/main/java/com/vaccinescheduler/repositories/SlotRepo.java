package com.vaccinescheduler.repositories;

import com.vaccinescheduler.models.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SlotRepo extends JpaRepository<Slot, Integer> {
    List<Slot> findByVaccineVaccineName(String vaccineName);
}
