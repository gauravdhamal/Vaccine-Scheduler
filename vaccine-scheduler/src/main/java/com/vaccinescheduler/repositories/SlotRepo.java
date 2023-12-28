package com.vaccinescheduler.repositories;

import com.vaccinescheduler.models.Slot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface SlotRepo extends JpaRepository<Slot, Integer> {
    @Query(value = "SELECT s.* " +
            "FROM slot s LEFT JOIN vaccine v " +
            "ON s.vaccine_vaccine_id = v.vaccine_id " +
            "WHERE v.vaccine_name = :vaccineName " +
            "AND " +
            "((s.slot_date = :currentDateEqual AND s.start_time > :currentTime " +
            "OR s.slot_date = :currentDateEqual AND s.start_time <= :currentTime AND s.end_time > :currentTime) " +
            "OR s.slot_date > :currentDate)",
            nativeQuery = true)
    List<Slot> findByVaccineVaccineNameAndSlotDateAfterOrSlotDateEqualsAndStartTimeAfter(String vaccineName, LocalDate currentDate, LocalDate currentDateEqual, LocalTime currentTime);
}
