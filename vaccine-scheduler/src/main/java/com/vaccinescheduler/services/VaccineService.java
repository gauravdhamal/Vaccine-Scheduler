package com.vaccinescheduler.services;

import com.vaccinescheduler.dtos.request.VaccineRequest;
import com.vaccinescheduler.dtos.response.VaccineResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.Vaccine;

import java.util.List;

public interface VaccineService {
    VaccineResponse createVaccine(VaccineRequest vaccineRequest) throws GeneralException;
    VaccineResponse getVaccine(Integer vaccineId) throws GeneralException;
    VaccineResponse updateVaccine(Integer vaccineId, VaccineRequest vaccineRequest) throws GeneralException;
    Boolean deleteVaccine(Integer vaccineId) throws GeneralException;
    List<VaccineResponse> getAllVaccines() throws GeneralException;
    List<VaccineResponse> getVaccinesByType(String type) throws GeneralException;
    List<VaccineResponse> findVaccineByName(String vaccineName) throws GeneralException;
}
