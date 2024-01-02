package com.vaccinescheduler.repositories;

import com.vaccinescheduler.models.AppointmentDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentDetailRepo extends JpaRepository<AppointmentDetail, Integer> {
    @Query("SELECT ad FROM AppointmentDetail ad\n" +
            "WHERE (ad.appointmentDate = :currentDate AND ad.appointmentTime = :appointmentTime AND ad.vaccinated = :vaccinated)\n" +
            "OR (ad.appointmentDate < :currentDate AND ad.vaccinated = :vaccinated)")
    Optional<List<AppointmentDetail>> findPastAppointments(LocalDate currentDate, String appointmentTime, Boolean vaccinated);
}
