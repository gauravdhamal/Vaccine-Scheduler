package com.vaccinescheduler.controllers;

import com.vaccinescheduler.dtos.request.PaymentDetailRequest;
import com.vaccinescheduler.dtos.response.PaymentDetailResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.services.PaymentDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentDetailService paymentDetailService;
    @PostMapping("/pay")
    public ResponseEntity<PaymentDetailResponse> createPaymentDetail(@RequestBody PaymentDetailRequest paymentDetailRequest) throws GeneralException {
        PaymentDetailResponse paymentDetailResponse = paymentDetailService.createPaymentDetail(paymentDetailRequest);
        return new ResponseEntity<>(paymentDetailResponse, HttpStatus.CREATED);
    }
    @PutMapping("/update/{paymentDetailId}")
    public ResponseEntity<PaymentDetailResponse> updatePaymentDetail(@PathVariable(value = "paymentDetailId") Integer paymentDetailId,@RequestBody PaymentDetailRequest paymentDetailRequest) throws GeneralException {
        PaymentDetailResponse paymentDetailResponse = paymentDetailService.updatePaymentDetail(paymentDetailId, paymentDetailRequest);
        return new ResponseEntity<>(paymentDetailResponse, HttpStatus.OK);
    }
    @GetMapping("/get/{paymentDetailId}")
    public ResponseEntity<PaymentDetailResponse> getPaymentDetail(@PathVariable(value = "paymentDetailId") Integer paymentDetailId) throws GeneralException {
        PaymentDetailResponse paymentDetailResponse = paymentDetailService.getPaymentDetail(paymentDetailId);
        return new ResponseEntity<>(paymentDetailResponse, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{paymentDetailId}")
    public ResponseEntity<Boolean> deletePaymentDetail(@PathVariable(value = "paymentDetailId") Integer paymentDetailId) throws GeneralException {
        Boolean deleted = paymentDetailService.deletePaymentDetail(paymentDetailId);
        return new ResponseEntity<>(deleted, HttpStatus.NO_CONTENT);
    }
}
