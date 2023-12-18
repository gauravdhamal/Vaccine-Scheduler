package com.vaccinescheduler.services;

import com.vaccinescheduler.dtos.request.VaccineRequest;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.Vaccine;

public interface VaccineService {
    Vaccine createVaccine(VaccineRequest vaccineRequest) throws GeneralException;
    Vaccine getVaccine(Integer vaccineId) throws GeneralException;
    Vaccine updateVaccine(Integer vaccineId, VaccineRequest vaccineRequest) throws GeneralException;
    Boolean deleteVaccine(Integer vaccineId) throws GeneralException;
}
