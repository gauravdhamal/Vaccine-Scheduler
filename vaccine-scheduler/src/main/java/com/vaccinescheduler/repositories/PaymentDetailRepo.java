package com.vaccinescheduler.repositories;

import com.vaccinescheduler.models.PaymentDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentDetailRepo extends JpaRepository<PaymentDetail, Integer> {
}
