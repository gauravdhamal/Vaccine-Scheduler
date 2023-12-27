package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.dtos.request.PaymentDetailRequest;
import com.vaccinescheduler.dtos.response.PaymentDetailResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.*;
import com.vaccinescheduler.repositories.AppointmentDetailRepo;
import com.vaccinescheduler.repositories.HospitalRepo;
import com.vaccinescheduler.repositories.PaymentDetailRepo;
import com.vaccinescheduler.repositories.PersonRepo;
import com.vaccinescheduler.services.PaymentDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Service
public class PaymentDetailServiceImpl implements PaymentDetailService {
    @Autowired
    private PaymentDetailRepo paymentDetailRepo;
    @Autowired
    private PersonRepo personRepo;
    @Autowired
    private AppointmentDetailRepo appointmentDetailRepo;
    @Autowired
    private HospitalRepo hospitalRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public PaymentDetailResponse createPaymentDetail(Integer appointmentDetailId, PaymentDetailRequest paymentDetailRequest) throws GeneralException {
        Optional<AppointmentDetail> appointmentDetailById = appointmentDetailRepo.findById(appointmentDetailId);
        if(appointmentDetailById.isPresent()) {
            AppointmentDetail appointmentDetail = appointmentDetailById.get();
            LocalDate appointmentDate = appointmentDetail.getAppointmentDate();
            String appointmentTime = appointmentDetail.getAppointmentTime();
            String startTimeString = appointmentTime.substring(0,5);
            String endTimeString = appointmentTime.substring(8,appointmentTime.length());
            LocalTime startTime = LocalTime.parse(startTimeString);
            LocalTime endTime = LocalTime.parse(endTimeString);
            LocalTime currentTime = LocalTime.now();
            LocalDate currentDate = LocalDate.now();
            if(appointmentDate.isAfter(currentDate) || (appointmentDate.isEqual(currentDate) && (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)))) {
                Double paidAmount = paymentDetailRequest.getAmount();
                Vaccine vaccine = appointmentDetail.getVaccine();
                Double requiredAmount = vaccine.getDiscountedPrice();
                if(paidAmount.equals(requiredAmount)) {
                    Person patient = modelMapper.map(appointmentDetail, Person.class);
                    patient.setAadhaarNumber(paymentDetailRequest.getAadhaarNumber());
                    patient.setUsername(paymentDetailRequest.getUsername());
                    patient.setPassword(paymentDetailRequest.getPassword());
                    patient.setAddress(paymentDetailRequest.getAddress());
                    patient.setLastName(paymentDetailRequest.getLastName());
                    patient.setDateOfBirth(paymentDetailRequest.getDateOfBirth());
                    String aadhaarNumber = patient.getAadhaarNumber();
                    Optional<Person> patientByAadhaarNumber = personRepo.findByAadhaarNumber(aadhaarNumber);
                    if(patientByAadhaarNumber.isPresent()) throw new GeneralException("Person already present in database with aadhaarNumber : { "+aadhaarNumber+" } Enter your aadhar number..");
                    String username = patient.getUsername();
                    Optional<Person> patientByUsername = personRepo.findByUsername(username);
                    if(patientByUsername.isPresent()) throw new GeneralException("Person already present in database with username : { "+username+" } Choose another username.");
                    patient.setRole("ROLE_PATIENT");
                    String encodedPassword = passwordEncoder.encode(patient.getPassword());
                    patient.setPassword(encodedPassword);
                    patient = personRepo.save(patient);
                    PaymentDetail paymentDetail = new PaymentDetail();
                    paymentDetail.setCreatedDateTime(LocalDateTime.now());
                    paymentDetail.setAmount(paidAmount);
                    paymentDetail.setPaymentMethod(paymentDetailRequest.getPaymentMethod());
                    paymentDetail.setTransactionStatus("success");
                    paymentDetail.setAppointmentDetail(appointmentDetail);
                    paymentDetail.setPatient(patient);
                    paymentDetail = paymentDetailRepo.save(paymentDetail);
                    appointmentDetail.setPaymentDetail(paymentDetail);
                    appointmentDetail.setPatient(patient);
                    Person doctor = appointmentDetail.getDoctor();
                    doctor.getDoctorAppointmentDetails().add(appointmentDetail);
                    personRepo.save(doctor);
                    Hospital hospital = appointmentDetail.getHospital();
                    hospital.getPaymentDetails().add(paymentDetail);
                    hospitalRepo.save(hospital);
                    PaymentDetailResponse paymentDetailResponse = modelMapper.map(paymentDetail, PaymentDetailResponse.class);
                    return paymentDetailResponse;
                } else {
                    throw new GeneralException("You need to pay the amount : { "+requiredAmount+" }. You entered : { "+paidAmount+" }. Please enter proper amount.");
                }
            } else {
                throw new GeneralException("Sorry you missed your appointment it was on : { "+appointmentDate+" } & time : { "+appointmentTime+" }. Kindly reschedule it and then do payment.");
            }
        } else {
            throw new GeneralException("AppointmentDetail not found with ID : { "+appointmentDetailId+" }. Enter correct Id.");
        }
    }

    @Override
    public PaymentDetailResponse updatePaymentDetail(Integer paymentDetailId, PaymentDetailRequest paymentDetailRequest) throws GeneralException {
        return null;
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
