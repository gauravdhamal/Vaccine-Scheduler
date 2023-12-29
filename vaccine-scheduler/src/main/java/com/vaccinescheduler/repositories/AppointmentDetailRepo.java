package com.vaccinescheduler.repositories;

import com.vaccinescheduler.models.AppointmentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AppointmentDetailRepo extends JpaRepository<AppointmentDetail, Integer> {
    Optional<List<AppointmentDetail>> findByAppointmentDateEqualsAndAppointmentTimeEqualsAndVaccinated(LocalDate currentDate, String appointmentTime, Boolean vaccinated);
}
