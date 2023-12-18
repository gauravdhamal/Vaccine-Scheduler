package com.vaccinescheduler.services;

import com.vaccinescheduler.dtos.request.AddSlots;
import com.vaccinescheduler.dtos.response.HospitalResponse;
import com.vaccinescheduler.dtos.response.PersonResponse;
import com.vaccinescheduler.exceptions.GeneralException;

import java.util.List;

public interface DoctorService {
    HospitalResponse getAssignedHospitalByDoctorId(Integer doctorId) throws GeneralException;
    String addSlotsByDoctorId(AddSlots addSlots) throws GeneralException;
    List<PersonResponse> getVaccinatedPatientsByDoctorId(Integer doctorId) throws GeneralException;
}
