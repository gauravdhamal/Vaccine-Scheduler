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
    @Autowired
    private JavaEmailService javaEmailService;
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
                    StringBuilder message = new StringBuilder("Dear ");
                    message.append(patient.getFirstName()).append(",\n\n")
                            .append("Thank you for your payment!\n\n")
                            .append("Payment details:\n")
                            .append("Amount Paid: ").append(paidAmount).append("\n")
                            .append("Payment Method: ").append(paymentMethod).append("\n\n")
                            .append("Details about you:\n")
                            .append("Name: ").append(patient.getFirstName()).append(" ").append(patient.getLastName()).append("\n")
                            .append("Gender: ").append(patient.getGender()).append("\n")
                            .append("Age: ").append(patient.getAge()).append("\n")
                            .append("City: ").append(patient.getAddress().getCity()).append("\n")
                            .append("Phone: ").append(patient.getAddress().getPhone()).append("\n")
                            .append("Email: ").append(patient.getAddress().getEmail()).append("\n\n")
                            .append("Thank you for your payment! You're all set for your vaccination appointment. Proceed with confidence!\n\n")
                            .append("Best regards,\n")
                            .append(hospital.getHospitalName()+".");
                    javaEmailService.sendEmail(patient.getAddress().getEmail(), "Payment confirmation mail from ~ [ "+hospital.getHospitalName()+" ]", message.toString());
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
