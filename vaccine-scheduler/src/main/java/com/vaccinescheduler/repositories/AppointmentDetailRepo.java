package com.vaccinescheduler.repositories;

import com.vaccinescheduler.models.AppointmentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentDetailRepo extends JpaRepository<AppointmentDetail, Integer> {
}
