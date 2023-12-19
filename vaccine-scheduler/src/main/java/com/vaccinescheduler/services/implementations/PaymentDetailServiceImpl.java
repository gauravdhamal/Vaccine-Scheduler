package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.dtos.request.PaymentDetailRequest;
import com.vaccinescheduler.dtos.response.PaymentDetailResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.AppointmentDetail;
import com.vaccinescheduler.models.PaymentDetail;
import com.vaccinescheduler.models.Person;
import com.vaccinescheduler.repositories.AppointmentDetailRepo;
import com.vaccinescheduler.repositories.PaymentDetailRepo;
import com.vaccinescheduler.repositories.PersonRepo;
import com.vaccinescheduler.services.PaymentDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.Optional;

public class PaymentDetailServiceImpl implements PaymentDetailService {
    @Autowired
    private PaymentDetailRepo paymentDetailRepo;
    @Autowired
    private PersonRepo personRepo;
    @Autowired
    private AppointmentDetailRepo appointmentDetailRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public PaymentDetailResponse createPaymentDetail(PaymentDetailRequest paymentDetailRequest) throws GeneralException {
        PaymentDetail paymentDetail = modelMapper.map(paymentDetailRequest, PaymentDetail.class);
        paymentDetail.setCreatedDateTime(LocalDateTime.now());
        paymentDetail.setTransactionStatus("success");
        Integer patientId = paymentDetailRequest.getPatientId();
        Optional<Person> personById = personRepo.findById(patientId);
        if(personById.isPresent()) {
            Person patient = personById.get();
            Integer appointmentDetailId = paymentDetailRequest.getAppointmentDetailId();
            Optional<AppointmentDetail> appointmentDetailById = appointmentDetailRepo.findById(appointmentDetailId);
            if(appointmentDetailById.isPresent()) {
                AppointmentDetail appointmentDetail = appointmentDetailById.get();
                if(appointmentDetail.getPaymentDetail() == null) {
                    boolean appointmentIdCheck = patient.getAppointmentDetails().stream().anyMatch(appointmentDetailTemp -> appointmentDetailTemp.getAppointmentDetailsId() == appointmentDetailId);
                    if(appointmentIdCheck) {
                        paymentDetail.setAppointmentDetail(appointmentDetail);
                        paymentDetail.setPatient(patient);
                        paymentDetail = paymentDetailRepo.save(paymentDetail);
                        appointmentDetail.setPaymentDetail(paymentDetail);
                        appointmentDetailRepo.save(appointmentDetail);
                        PaymentDetailResponse paymentDetailResponse = modelMapper.map(paymentDetail, PaymentDetailResponse.class);
                        return paymentDetailResponse;
                    } else {
                        throw new GeneralException("Provided appointment Id { "+appointmentDetailId+" { does not belongs to patient with Id : { "+patientId+" }");
                    }
                } else {
                    throw new GeneralException("Appointment already linked to another payment receipt. Provide correct appointmentId : "+appointmentDetailId);
                }
            } else {
                throw new GeneralException("Appointment not found with ID : "+appointmentDetailId);
            }
        } else {
            throw new GeneralException("Patient not found with ID : "+patientId);
        }
    }

    @Override
    public PaymentDetailResponse updatePaymentDetail(Integer paymentDetailId, PaymentDetailRequest paymentDetailRequest) throws GeneralException {
        Optional<PaymentDetail> paymentDetailById = paymentDetailRepo.findById(paymentDetailId);
        if(paymentDetailById.isPresent()) {
            Integer patientId = paymentDetailRequest.getPatientId();
            Integer appointmentDetailId = paymentDetailRequest.getAppointmentDetailId();
            PaymentDetail updatedPaymentDetail = modelMapper.map(paymentDetailRequest, PaymentDetail.class);
            PaymentDetail oldPaymentDetail = paymentDetailById.get();
            if (updatedPaymentDetail.getPaymentMethod() != null) {
                oldPaymentDetail.setPaymentMethod(updatedPaymentDetail.getPaymentMethod());
            }
            if (updatedPaymentDetail.getAmount() != null) {
                oldPaymentDetail.setAmount(updatedPaymentDetail.getAmount());
            }
            if(oldPaymentDetail.getPatient().getPersonId() != patientId) {
                Optional<Person> patientById = personRepo.findById(patientId);
                if(patientById.isPresent()) {
                    oldPaymentDetail.setPatient(patientById.get());
                }
            }
            if(oldPaymentDetail.getAppointmentDetail().getAppointmentDetailsId() != appointmentDetailId) {
                Optional<AppointmentDetail> appointmentDetailById = appointmentDetailRepo.findById(appointmentDetailId);
                if(appointmentDetailById.isPresent()) {
                    oldPaymentDetail.setAppointmentDetail(appointmentDetailById.get());
                }
            }
            oldPaymentDetail = paymentDetailRepo.save(oldPaymentDetail);
            PaymentDetailResponse paymentDetailResponse = modelMapper.map(oldPaymentDetail, PaymentDetailResponse.class);
            return paymentDetailResponse;
        } else {
            throw new GeneralException("Payment detail not found with Id : "+paymentDetailId);
        }
    }

    @Override
    public PaymentDetailResponse getPaymentDetail(Integer paymentDetailId) throws GeneralException {
        Optional<PaymentDetail> paymentDetailById = paymentDetailRepo.findById(paymentDetailId);
        if(paymentDetailById.isPresent()) {
            PaymentDetail paymentDetail = paymentDetailById.get();
            PaymentDetailResponse paymentDetailResponse = modelMapper.map(paymentDetail, PaymentDetailResponse.class);
            return paymentDetailResponse;
        } else {
            throw new GeneralException("Payment detail not found with Id : "+paymentDetailId);
        }
    }

    @Override
    public Boolean deletePaymentDetail(Integer paymentDetailId) throws GeneralException {
        Optional<PaymentDetail> paymentDetailById = paymentDetailRepo.findById(paymentDetailId);
        if(paymentDetailById.isPresent()) {
            PaymentDetail paymentDetail = paymentDetailById.get();
            paymentDetailRepo.delete(paymentDetail);
            return true;
        } else {
            throw new GeneralException("Payment detail not found with Id : "+paymentDetailId);
        }
    }
}
