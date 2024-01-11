package com.vaccinescheduler.services;

import com.vaccinescheduler.dtos.request.ExistingPaymentDetailRequest;
import com.vaccinescheduler.dtos.request.PaymentDetailRequest;
import com.vaccinescheduler.dtos.response.PaymentDetailResponse;
import com.vaccinescheduler.exceptions.GeneralException;

import java.io.IOException;

public interface PaymentDetailService {
    PaymentDetailResponse createPaymentDetail(Integer appointmentDetailId, PaymentDetailRequest paymentDetailRequest) throws GeneralException, IOException;
    PaymentDetailResponse createPaymentDetailForExistingPerson(String username, ExistingPaymentDetailRequest existingPaymentDetailRequest) throws GeneralException;
    PaymentDetailResponse updatePaymentDetail(Integer paymentDetailId, PaymentDetailRequest paymentDetailRequest) throws GeneralException;
    PaymentDetailResponse getPaymentDetail(Integer paymentDetailId) throws GeneralException;
    Boolean deletePaymentDetail(Integer paymentDetailId) throws GeneralException;
}
