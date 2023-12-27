package com.vaccinescheduler.services;

import com.vaccinescheduler.dtos.request.AddSlotsRequest;
import com.vaccinescheduler.dtos.response.AppointmentResponse;
import com.vaccinescheduler.dtos.response.HospitalResponse;
import com.vaccinescheduler.dtos.response.PersonResponse;
import com.vaccinescheduler.dtos.response.SlotResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.Person;

import java.util.List;

public interface DoctorService {
    HospitalResponse getAssignedHospitalByDoctorId(Integer doctorId) throws GeneralException;
    String addSlotsByDoctorId(AddSlotsRequest addSlotsRequest) throws GeneralException;
    List<PersonResponse> getVaccinatedPatientsByDoctorId(Integer doctorId) throws GeneralException;
    List<PersonResponse> getPatientsFromAppointmentsByDoctorId(Integer doctorId) throws GeneralException;
    List<AppointmentResponse> getAppointmentDetailsByDoctorId(Integer doctorId) throws GeneralException;
//    List<AppointmentResponse> getVaccinationDetailsByDoctorId(Integer doctorId) throws GeneralException;
    List<PersonResponse> getAllDoctors() throws GeneralException;
    List<SlotResponse> getAllSlots(Integer doctorId) throws GeneralException;
    Person getOriginalDoctor(Integer doctorId) throws GeneralException;
}
