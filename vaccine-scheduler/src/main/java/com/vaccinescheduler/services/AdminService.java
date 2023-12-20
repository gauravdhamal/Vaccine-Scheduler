package com.vaccinescheduler.services;

import com.vaccinescheduler.exceptions.GeneralException;

public interface AdminService {
    String updateInventoryManager(Integer adminId, Integer inventoryId) throws GeneralException;
}
