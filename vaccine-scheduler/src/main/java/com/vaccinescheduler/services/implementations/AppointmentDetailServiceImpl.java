package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.dtos.request.AppointmentDetailRequest;
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
    private SlotRepo slotRepo;
    @Autowired
    private HospitalRepo hospitalRepo;
    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private AppointmentDetailRepo appointmentDetailRepo;

    @Override
    public AppointmentDetailResponse getAppointmentDetail(Integer appointmentDetailId) throws GeneralException {
        Optional<AppointmentDetail> appointmentDetailById = appointmentDetailRepo.findById(appointmentDetailId);
        if(appointmentDetailById.isPresent()) {
            AppointmentDetail appointmentDetail = appointmentDetailById.get();
            AppointmentDetailResponse appointmentDetailResponse = modelMapper.map(appointmentDetail, AppointmentDetailResponse.class);
            String message = "Dear " + appointmentDetail.getFirstName() + ", your appointment has been booked. "
                    + "We look forward to providing you with excellent service. "
                    + "Details: Gender - " + appointmentDetail.getGender() + ", Age - " + appointmentDetail.getAge() + ", Phone - " + appointmentDetail.getPhone() + ", Email - " + appointmentDetail.getEmail();
            appointmentDetailResponse.setMessage(message);
            return appointmentDetailResponse;
        } else {
            throw new GeneralException("No any appointment found with ID : "+appointmentDetailId);
        }
    }

    @Override
    public AppointmentDetailResponse bookAppointment(Integer slotId, Integer hospitalId, AppointmentDetailRequest appointmentDetailRequest) throws GeneralException {
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
                                    Integer currentAge = appointmentDetailRequest.getAge();
                                    if(currentAge >= minAgeReq && currentAge <= maxAgeReq) {
                                        AppointmentDetail appointmentDetail = modelMapper.map(appointmentDetailRequest, AppointmentDetail.class);
                                        appointmentDetail.setAppointmentDate(slot.getSlotDate());
                                        appointmentDetail.setVaccine(requiredVaccine);
                                        appointmentDetail.setCreatedAt(LocalDateTime.now());
                                        appointmentDetail.setDoctor(requiredDoctor);
                                        appointmentDetail.setVaccinated(false);
                                        appointmentDetail.setAppointmentTime(slot.getStartTime() + " - " + slot.getEndTime());
                                        appointmentDetail.setHospital(hospital);
                                        Inventory inventory = hospital.getInventory();
                                        Integer vaccineCount = inventory.getVaccineCount();
                                        vaccineCount--;
                                        inventory.setVaccineCount(vaccineCount);
                                        Integer availableCount = slot.getAvailableCount();
                                        availableCount--;
                                        slot.setAvailableCount(availableCount);
                                        slotRepo.save(slot);
                                        inventoryRepo.save(inventory);
                                        appointmentDetail = appointmentDetailRepo.save(appointmentDetail);
                                        hospital.getAppointmentDetails().add(appointmentDetail);
                                        hospitalRepo.save(hospital);
                                        AppointmentDetailResponse appointmentDetailResponse = modelMapper.map(appointmentDetail, AppointmentDetailResponse.class);
                                        String message = "Dear " + appointmentDetailRequest.getFirstName() + ", your appointment has been booked. "
                                                + "We look forward to providing you with excellent service. "
                                                + "Details: Gender - " + appointmentDetailRequest.getGender() + ", Age - " + appointmentDetailRequest.getAge() + ", Phone - " + appointmentDetailRequest.getPhone() + ", Email - " + appointmentDetailRequest.getEmail();
                                        appointmentDetailResponse.setMessage(message);
                                        return appointmentDetailResponse;
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
