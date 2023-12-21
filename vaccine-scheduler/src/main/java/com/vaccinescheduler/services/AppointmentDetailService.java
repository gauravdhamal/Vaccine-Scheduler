package com.vaccinescheduler.services;

import com.vaccinescheduler.dtos.request.BasicDetailsRequest;
import com.vaccinescheduler.dtos.response.AppointmentDetailResponse;
import com.vaccinescheduler.exceptions.GeneralException;

public interface AppointmentDetailService {
    AppointmentDetailResponse bookAppointment(Integer slotId, Integer hospitalId, BasicDetailsRequest basicDetailsRequest) throws GeneralException;
}
