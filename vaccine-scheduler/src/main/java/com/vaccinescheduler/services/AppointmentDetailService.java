package com.vaccinescheduler.services;

import com.vaccinescheduler.dtos.request.AppointmentDetailRequest;
import com.vaccinescheduler.dtos.response.AppointmentDetailResponse;
import com.vaccinescheduler.exceptions.GeneralException;

public interface AppointmentDetailService {
    AppointmentDetailResponse getAppointmentDetail(Integer appointmentDetailId) throws GeneralException;
    AppointmentDetailResponse bookAppointment(Integer slotId, Integer hospitalId, AppointmentDetailRequest appointmentDetailRequest) throws GeneralException;
}
