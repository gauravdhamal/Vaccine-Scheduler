package com.vaccinescheduler.services;

import com.vaccinescheduler.dtos.response.PersonResponse;
import com.vaccinescheduler.exceptions.GeneralException;

import java.util.List;

public interface AdminService {
    String updateInventoryManager(Integer adminId, Integer inventoryId) throws GeneralException;
    List<PersonResponse> getAllAdmins() throws GeneralException;
}
