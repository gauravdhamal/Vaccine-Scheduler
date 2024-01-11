package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.dtos.other.PaymentData;
import com.vaccinescheduler.dtos.request.ExistingPaymentDetailRequest;
import com.vaccinescheduler.dtos.request.PaymentDetailRequest;
import com.vaccinescheduler.dtos.response.PaymentDetailResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.*;
import com.vaccinescheduler.repositories.AppointmentDetailRepo;
import com.vaccinescheduler.repositories.HospitalRepo;
import com.vaccinescheduler.repositories.PaymentDetailRepo;
import com.vaccinescheduler.repositories.PersonRepo;
import com.vaccinescheduler.services.CsvService;
import com.vaccinescheduler.services.PaymentDetailService;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    @Autowired
    private JavaEmailService javaEmailService;
    @Autowired
    private CsvService csvService;
    @Autowired
    private ProducerTemplate producerTemplate;
    @Override
    public PaymentDetailResponse createPaymentDetail(Integer appointmentDetailId, PaymentDetailRequest paymentDetailRequest) throws GeneralException, IOException {
        Optional<AppointmentDetail> appointmentDetailById = appointmentDetailRepo.findById(appointmentDetailId);
        if(appointmentDetailById.isPresent()) {
            AppointmentDetail appointmentDetail = appointmentDetailById.get();
            if(appointmentDetail.getPaymentDetail() == null) {
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
                        String paymentMethod = paymentDetailRequest.getPaymentMethod();
                        paymentDetail.setPaymentMethod(paymentMethod);
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

                        PaymentData paymentData = new PaymentData();
                        paymentData.setPaymentMethod(paymentMethod);
                        paymentData.setNotified(true);
                        paymentData.setHospitalName(hospital.getHospitalName());
                        paymentData.setPaidAmount(paidAmount);
                        paymentData.setPatientCity(patient.getAddress().getCity());
                        paymentData.setPatientEmail(patient.getAddress().getEmail());
                        paymentData.setPatientAge(patient.getAge());
                        paymentData.setPatientName(patient.getFirstName());
                        paymentData.setPatientPhone(patient.getAddress().getPhone());
                        paymentData.setPatientGender(patient.getGender());
                        Exchange exchange = new DefaultExchange(new DefaultCamelContext());
                        exchange.getIn().setBody(paymentData);
                        producerTemplate.send("seda:processPaymentNotification", exchange);

                        return paymentDetailResponse;
                    } else {
                        throw new GeneralException("You need to pay the amount : { "+requiredAmount+" }. You entered : { "+paidAmount+" }. Please enter proper amount.");
                    }
                } else {
                    throw new GeneralException("Sorry you missed your appointment it was on : { "+appointmentDate+" } & time : { "+appointmentTime+" }. Kindly reschedule it and then do payment.");
                }
            } else {
                throw new GeneralException("Payment already made for this appointment can't proceed . Payment Id : { "+appointmentDetail.getPaymentDetail().getPaymentId()+" }.");
            }
        } else {
            throw new GeneralException("AppointmentDetail not found with ID : { "+appointmentDetailId+" }. Enter correct Id.");
        }
    }

    @Override
    public PaymentDetailResponse createPaymentDetailForExistingPerson(String username, ExistingPaymentDetailRequest existingPaymentDetailRequest) throws GeneralException {
        Integer appointmentDetailId = existingPaymentDetailRequest.getAppointmentDetailId();
        Double paidAmount  = existingPaymentDetailRequest.getPaidAmount();
        String paymentMethod = existingPaymentDetailRequest.getPaymentMethod();
        Optional<Person> personByUsername = personRepo.findByUsername(username);
        if(personByUsername.isPresent()) {
            Person patient = personByUsername.get();
            Optional<AppointmentDetail> appointmentDetailById = appointmentDetailRepo.findById(appointmentDetailId);
            if(appointmentDetailById.isPresent()) {
                AppointmentDetail appointmentDetail = appointmentDetailById.get();
                if(appointmentDetail.getPaymentDetail() == null) {
                    LocalDate appointmentDate = appointmentDetail.getAppointmentDate();
                    String appointmentTime = appointmentDetail.getAppointmentTime();
                    String startTimeString = appointmentTime.substring(0,5);
                    String endTimeString = appointmentTime.substring(8,appointmentTime.length());
                    LocalTime startTime = LocalTime.parse(startTimeString);
                    LocalTime endTime = LocalTime.parse(endTimeString);
                    LocalTime currentTime = LocalTime.now();
                    LocalDate currentDate = LocalDate.now();
                    if(appointmentDate.isAfter(currentDate) || (appointmentDate.isEqual(currentDate) && (currentTime.isAfter(startTime) && currentTime.isBefore(endTime)))) {
                        Vaccine vaccine = appointmentDetail.getVaccine();
                        Double requiredAmount = vaccine.getDiscountedPrice();
                        if(paidAmount.equals(requiredAmount)) {
                            patient.getAppointmentDetailsForPatients().add(appointmentDetail);
                            patient = personRepo.save(patient);
                            PaymentDetail paymentDetail = new PaymentDetail();
                            paymentDetail.setCreatedDateTime(LocalDateTime.now());
                            paymentDetail.setAmount(paidAmount);
                            paymentDetail.setPaymentMethod(paymentMethod);
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

                            PaymentData paymentData = new PaymentData();
                            paymentData.setPaymentMethod(paymentMethod);
                            paymentData.setNotified(true);
                            paymentData.setHospitalName(hospital.getHospitalName());
                            paymentData.setPaidAmount(paidAmount);
                            paymentData.setPatientCity(patient.getAddress().getCity());
                            paymentData.setPatientEmail(patient.getAddress().getEmail());
                            paymentData.setPatientAge(patient.getAge());
                            paymentData.setPatientName(patient.getFirstName());
                            paymentData.setPatientPhone(patient.getAddress().getPhone());
                            paymentData.setPatientGender(patient.getGender());
                            Exchange exchange = new DefaultExchange(new DefaultCamelContext());
                            exchange.getIn().setBody(paymentData);
                            producerTemplate.send("seda:processPaymentNotification", exchange);

                            return paymentDetailResponse;
                        } else {
                            throw new GeneralException("You need to pay the amount : { "+requiredAmount+" }. You entered : { "+paidAmount+" }. Please enter proper amount.");
                        }
                    } else {
                        throw new GeneralException("Sorry you missed your appointment it was on : { "+appointmentDate+" } & time : { "+appointmentTime+" }. Kindly reschedule it and then do payment.");
                    }
                } else {
                    throw new GeneralException("Payment already made for this appointment can't proceed . Payment Id : { "+appointmentDetail.getPaymentDetail().getPaymentId()+" }.");
                }
            } else {
                throw new GeneralException("AppointmentDetail not found with ID : { "+appointmentDetailId+" }. Enter correct Id.");
            }
        } else {
            throw new GeneralException("Person not found with username : "+username);
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
