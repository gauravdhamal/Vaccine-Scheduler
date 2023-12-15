package com.vaccinescheduler.repositories;

import com.vaccinescheduler.models.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepo extends JpaRepository<Inventory, Integer> {
}
