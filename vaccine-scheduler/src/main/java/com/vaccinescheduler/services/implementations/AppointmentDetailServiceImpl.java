package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.dtos.request.BasicDetailsRequest;
import com.vaccinescheduler.dtos.response.AppointmentDetailResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.*;
import com.vaccinescheduler.repositories.*;
import com.vaccinescheduler.services.AppointmentDetailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentDetailServiceImpl implements AppointmentDetailService {
    @Autowired
    private PersonRepo personRepo;
    @Autowired
    private SlotRepo slotRepo;
    @Autowired
    private HospitalRepo hospitalRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AppointmentDetailRepo appointmentDetailRepo;
    @Override
    public AppointmentDetailResponse bookAppointment(Integer slotId, Integer hospitalId, BasicDetailsRequest basicDetailsRequest) throws GeneralException {
        Optional<Slot> slotById = slotRepo.findById(slotId);
        if(slotById.isPresent()) {
            Slot slot = slotById.get();
            Person requiredDoctor = slot.getDoctor();
            Vaccine requiredVaccine = slot.getVaccine();
            Optional<Hospital> hospitalById = hospitalRepo.findById(hospitalId);
            if(hospitalById.isPresent()) {
                Hospital hospital = hospitalById.get();
                if(hospital.getInventory() != null) {
                    if(hospital.getInventory().getVaccineCount() > 0 && !hospital.getInventory().getVaccines().isEmpty()) {
                        if(!hospital.getDoctors().isEmpty()) {
                            List<Person> doctors = hospital.getDoctors();
                            boolean doctorAvailabilityCheck = doctors.stream().anyMatch(doctor -> doctor.getPersonId() == requiredDoctor.getPersonId());
                            if(doctorAvailabilityCheck) {
                                List<Vaccine> vaccines = hospital.getInventory().getVaccines();
                                boolean vaccineAvailabilityCheck = vaccines.stream().anyMatch(vaccine -> vaccine.getVaccineId() == requiredVaccine.getVaccineId());
                                if(vaccineAvailabilityCheck) {
                                    Integer minAgeReq = requiredVaccine.getMinAge();
                                    Integer maxAgeReq = requiredVaccine.getMaxAge();
                                    Integer currentAge = basicDetailsRequest.getAge();
                                    if(currentAge >= minAgeReq && currentAge <= maxAgeReq) {
                                        Person patient = modelMapper.map(basicDetailsRequest, Person.class);
                                        patient.setRole("ROLE_PATIENT");
                                        String aadhaarNumber = patient.getAadhaarNumber();
                                        Optional<Person> patientByAadhaarNumber = personRepo.findByAadhaarNumber(aadhaarNumber);
                                        if(!patientByAadhaarNumber.isPresent()) {
                                            personRepo.save(patient);
                                            AppointmentDetail appointmentDetail = new AppointmentDetail();
                                            appointmentDetail.setAppointmentDate(slot.getSlotDate());
                                            appointmentDetail.setVaccine(requiredVaccine);
                                            appointmentDetail.setCreatedAt(LocalDateTime.now());
                                            appointmentDetail.setDoctor(requiredDoctor);
                                            appointmentDetail.setVaccinated(false);
                                            appointmentDetail.setAppointmentTime(slot.getStartTime() + " - " + slot.getEndTime());
                                            appointmentDetail.setPatient(patient);
                                            appointmentDetail = appointmentDetailRepo.save(appointmentDetail);
                                            hospital.getAppointmentDetails().add(appointmentDetail);
                                            patient.getAppointmentDetails().add(appointmentDetail);
                                            hospitalRepo.save(hospital);
                                            AppointmentDetailResponse appointmentDetailResponse = modelMapper.map(appointmentDetail, AppointmentDetailResponse.class);
                                            appointmentDetailResponse.setMessage("Please be there 15 minutes before start time to make the payment and avoid any on time delays.");
                                            return appointmentDetailResponse;
                                        } else {
                                            throw new GeneralException("Patient is already present with aadhaar number : { "+aadhaarNumber+" }. Enter correct aadhaar number.");
                                        }
                                    } else {
                                        throw new GeneralException("You are not allowed to take this vaccine as your age : "+currentAge+" is not in the range of : ( "+minAgeReq+" - "+maxAgeReq+" ).");
                                    }
                                } else {
                                    throw new GeneralException("Your selected vaccine ( ID : "+requiredVaccine.getVaccineId()+", Name : "+requiredVaccine.getVaccineName()+" ) is not available in inventory. Check after some time.");
                                }
                            } else {
                                throw new GeneralException("Your selected doctor ( ID : "+requiredDoctor.getPersonId()+", Username : "+requiredDoctor.getUsername()+" ) is not available at hospital. Check after some time.");
                            }
                        } else {
                            throw new GeneralException("No any doctor available in hospital right now. Check after some time");
                        }
                    } else {
                        throw new GeneralException("No any vaccine available in inventory. Check again tomorrow.");
                    }
                } else {
                    throw new GeneralException("Inventory not available at selected hospital ID : { "+hospitalId+" }. Go to another hospital.");
                }
            } else {
                throw new GeneralException("Hospital not found with ID : { "+hospitalId+" }. Enter correct ID.");
            }
        } else {
            throw new GeneralException("Slot not found with ID : { "+slotId+" }. Enter correct ID.");
        }
    }
}
