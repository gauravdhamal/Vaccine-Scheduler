package com.vaccinescheduler.services;

import com.vaccinescheduler.dtos.request.PaymentDetailRequest;
import com.vaccinescheduler.dtos.response.PaymentDetailResponse;
import com.vaccinescheduler.exceptions.GeneralException;

public interface PaymentDetailService {
    PaymentDetailResponse createPaymentDetail(PaymentDetailRequest paymentDetailRequest) throws GeneralException;
    PaymentDetailResponse updatePaymentDetail(Integer paymentDetailId, PaymentDetailRequest paymentDetailRequest) throws GeneralException;
    PaymentDetailResponse getPaymentDetail(Integer paymentDetailId) throws GeneralException;
    Boolean deletePaymentDetail(Integer paymentDetailId) throws GeneralException;
}
