package com.vaccinescheduler.services;

import com.vaccinescheduler.dtos.response.VaccinationResponse;
import com.vaccinescheduler.exceptions.GeneralException;

import java.time.LocalDate;
import java.util.List;

public interface VaccinationDetailService {
    List<VaccinationResponse> getVaccinationDetailsByDateAndSlot(LocalDate date, String slot) throws GeneralException;
    List<VaccinationResponse> updateVaccinationRecord() throws GeneralException;
    List<VaccinationResponse> getAllVaccinationRecords() throws GeneralException;
}
