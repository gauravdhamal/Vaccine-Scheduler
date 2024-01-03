package com.vaccinescheduler.services;

import com.vaccinescheduler.dtos.other.VaccinationData;
import com.vaccinescheduler.dtos.request.AppointmentDetailRequest;
import com.vaccinescheduler.dtos.request.PaymentDetailRequest;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.*;

import java.io.IOException;

public interface CsvService {
    String bookingDataToCSV(AppointmentDetailRequest appointmentDetailRequest, Slot slot, Hospital hospital, Person doctor, Vaccine vaccine) throws IOException;
    String reschedulingDataToCSV(AppointmentDetail appointmentDetail, Slot slot, Hospital hospital, Person doctor, String vaccineName) throws IOException;
    String vaccinatedDataToCSV(VaccinationData vaccinationData) throws GeneralException;
    String paymentDataToCSV(PaymentDetailRequest paymentDetailRequest, Person patient, Hospital hospital) throws IOException;
}
