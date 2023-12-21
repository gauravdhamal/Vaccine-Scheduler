package com.vaccinescheduler.services;

import com.vaccinescheduler.dtos.request.DoctorListRrquest;
import com.vaccinescheduler.dtos.request.HospitalRequest;
import com.vaccinescheduler.dtos.response.AppointmentResponse;
import com.vaccinescheduler.dtos.response.HospitalResponse;
import com.vaccinescheduler.dtos.response.PaymentDetailResponse;
import com.vaccinescheduler.exceptions.GeneralException;

import java.util.List;

public interface HospitalService {
    HospitalResponse createHospital(HospitalRequest hospitalRequest) throws GeneralException;
    HospitalResponse getHospital(Integer hospitalId) throws GeneralException;
    HospitalResponse updateHospital(Integer hospitalId, HospitalRequest hospitalRequest) throws GeneralException;
    Boolean deleteHospital(Integer hospitalId) throws GeneralException;
    String addInventoryToHospital(Integer hospitalId, Integer inventoryId) throws GeneralException;
    List<PaymentDetailResponse> getAllPayments(Integer hospitalId) throws GeneralException;
    List<AppointmentResponse> getAllAppointments(Integer hospitalId) throws GeneralException;
    String addDoctorsToHospital(DoctorListRrquest doctorListRrquest) throws GeneralException;
    List<HospitalResponse> getAllHospitals() throws GeneralException;
}
