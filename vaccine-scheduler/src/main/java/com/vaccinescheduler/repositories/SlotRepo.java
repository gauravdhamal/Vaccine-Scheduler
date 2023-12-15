package com.vaccinescheduler.repositories;

import com.vaccinescheduler.models.Slot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SlotRepo extends JpaRepository<Slot, Integer> {
}
