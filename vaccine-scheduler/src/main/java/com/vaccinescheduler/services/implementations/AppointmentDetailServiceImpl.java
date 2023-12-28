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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private PersonRepo personRepo;
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
            LocalDate slotDate = slot.getSlotDate();
            LocalTime startTime = slot.getStartTime();
            LocalTime endTime = slot.getEndTime();
            LocalDate currentDate = LocalDate.now();
            LocalTime currentTime = LocalTime.now();
            boolean futureDateCheck = slotDate.isAfter(currentDate);
            boolean todayButStartTimeIsAfterCurrTime = slotDate.isEqual(currentDate) && startTime.isAfter(currentTime);
            boolean todayStartedButYetNotEnded = slotDate.isEqual(currentDate) && startTime.isBefore(currentTime) && endTime.isAfter(currentTime);
            if(futureDateCheck || todayButStartTimeIsAfterCurrTime || todayStartedButYetNotEnded) {
                if(slot.getSlotCount() > 0) {
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
                                                appointmentDetail.setAppointmentTime(slot.getStartTime() + " - " + slot.getEndTime());
                                                appointmentDetail.setHospital(hospital);
                                                appointmentDetail.setDoseNumber(appointmentDetailRequest.getDoseNumber());
                                                appointmentDetail.setSlot(slot);
                                                Inventory inventory = hospital.getInventory();
                                                Integer vaccineCount = inventory.getVaccineCount();
                                                vaccineCount--;
                                                inventory.setVaccineCount(vaccineCount);
                                                Integer slotCount = slot.getSlotCount();
                                                slotCount--;
                                                slot.setSlotCount(slotCount);
                                                slotRepo.save(slot);
                                                inventoryRepo.save(inventory);
                                                appointmentDetail = appointmentDetailRepo.save(appointmentDetail);
                                                hospital.getAppointmentDetails().add(appointmentDetail);
                                                hospitalRepo.save(hospital);
                                                requiredDoctor.getDoctorAppointmentDetails().add(appointmentDetail);
                                                personRepo.save(requiredDoctor);
                                                AppointmentDetailResponse appointmentDetailResponse = modelMapper.map(appointmentDetail, AppointmentDetailResponse.class);
                                                String message = "Dear " + appointmentDetailRequest.getFirstName() + ", your appointment has been booked. "
                                                        + "We look forward to providing you with excellent service. "
                                                        + "Details: Gender - " + appointmentDetailRequest.getGender() + ", Age - " + appointmentDetailRequest.getAge() + ", Phone - " + appointmentDetailRequest.getPhone() + ", Email - " + appointmentDetailRequest.getEmail();
                                                if(todayStartedButYetNotEnded) message = message + ". You are having very less time as your slotTime will end soon. Kindly make payment ASAP and take the vaccination.";
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
                                throw new GeneralException("No any vaccine available in inventory of hospital ID : { "+hospitalId+" }. Go to another hospital.");
                            }
                        } else {
                            throw new GeneralException("Inventory not available at selected hospital ID : { "+hospitalId+" }. Go to another hospital.");
                        }
                    } else {
                        throw new GeneralException("Hospital not found with ID : { "+hospitalId+" }. Enter correct ID.");
                    }
                } else {
                    throw new GeneralException("All slots are booked no any slot available. Available slots : "+slot.getSlotCount()+". Try for another slot.");
                }
            } else {
                throw new GeneralException("Sorry slot is currently expired. slotDate : { "+slotDate+" }, startTime : { "+startTime+" }, endTime : { "+endTime+" }. Please try for another slot.");
            }
        } else {
            throw new GeneralException("Slot not found with ID : { "+slotId+" }. Enter correct ID.");
        }
    }

    @Override
    public AppointmentDetailResponse rescheduleAppointment(Integer newSlotId, Integer appointmentId) throws GeneralException {
        Optional<AppointmentDetail> appointmentDetailById = appointmentDetailRepo.findById(appointmentId);
        if(appointmentDetailById.isPresent()) {
            AppointmentDetail appointmentDetail = appointmentDetailById.get();
            Slot oldSlot = appointmentDetail.getSlot();
            Integer oldSlotCount = oldSlot.getSlotCount();
            if(!oldSlot.getSlotId().equals(newSlotId)) {
                Hospital hospital = appointmentDetail.getHospital();
                Optional<Slot> newSlotById = slotRepo.findById(newSlotId);
                if (newSlotById.isPresent()) {
                    Slot newSlot = newSlotById.get();
                    Person newSlotDoctor = newSlot.getDoctor();
                    Integer newSlotCount = newSlot.getSlotCount();
                    if(newSlotCount > 0) {
                        if(oldSlot.getVaccine().getVaccineName().equals(newSlot.getVaccine().getVaccineName())) {
                            newSlotCount--;
                            newSlot.setSlotCount(newSlotCount);
                            oldSlotCount++;
                            oldSlot.setSlotCount(oldSlotCount);
                            appointmentDetail.setSlot(newSlot);
                            appointmentDetail.setDoctor(newSlotDoctor);
                            appointmentDetail.setAppointmentTime(newSlot.getStartTime() + " - " + newSlot.getEndTime());
                            appointmentDetail.setAppointmentDate(newSlot.getSlotDate());
                            appointmentDetail.setCreatedAt(LocalDateTime.now());
                            appointmentDetail = appointmentDetailRepo.save(appointmentDetail);
                            slotRepo.save(newSlot);
                            slotRepo.save(oldSlot);
                            AppointmentDetailResponse appointmentDetailResponse = modelMapper.map(appointmentDetail, AppointmentDetailResponse.class);
                            String message = "Dear " + appointmentDetail.getFirstName() + ", your appointment has been rescheduled. Kindle make payment if not done already."
                                    + "Details: Gender - " + appointmentDetail.getGender() + ", Age - " + appointmentDetail.getAge() + ", Phone - " + appointmentDetail.getPhone() + ", Email - " + appointmentDetail.getEmail();
                            appointmentDetailResponse.setMessage(message);
                            return appointmentDetailResponse;
                        } else {
                            throw new GeneralException("Selected slot does not have required vaccine : "+oldSlot.getVaccine().getVaccineName());
                        }
                    } else {
                        throw new GeneralException("All slots are booked no any slot available. Available slots : "+newSlot.getSlotCount()+". Try for another slot.");
                    }
                } else {
                    throw new GeneralException("New Slot not found with ID : " + newSlotId);
                }
            } else {
                throw new GeneralException("Enter new slot Id. Old slotId : { "+oldSlot.getSlotId()+" } & New slotId : { "+newSlotId+" } both are same.");
            }
        } else {
            throw new GeneralException("Appointment not found with ID : "+appointmentId);
        }
    }
}
