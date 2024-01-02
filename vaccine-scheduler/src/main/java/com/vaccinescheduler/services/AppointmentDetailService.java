package com.vaccinescheduler.services;

import com.vaccinescheduler.dtos.request.AppointmentDetailRequest;
import com.vaccinescheduler.dtos.response.AppointmentDetailResponse;
import com.vaccinescheduler.exceptions.GeneralException;

import java.io.IOException;

public interface AppointmentDetailService {
    AppointmentDetailResponse getAppointmentDetail(Integer appointmentDetailId) throws GeneralException;
    AppointmentDetailResponse bookAppointment(Integer slotId, Integer hospitalId, AppointmentDetailRequest appointmentDetailRequest) throws GeneralException, IOException;
    AppointmentDetailResponse rescheduleAppointment(Integer newSlotId, Integer appointmentId) throws GeneralException, IOException;
}
