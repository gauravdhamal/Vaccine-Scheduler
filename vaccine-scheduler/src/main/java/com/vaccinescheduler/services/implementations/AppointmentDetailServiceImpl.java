package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.dtos.other.AppointmentData;
import com.vaccinescheduler.dtos.request.AppointmentDetailRequest;
import com.vaccinescheduler.dtos.response.AppointmentDetailResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.*;
import com.vaccinescheduler.repositories.*;
import com.vaccinescheduler.services.AppointmentDetailService;
import com.vaccinescheduler.services.CsvService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.impl.engine.DefaultProducerTemplate;
import org.apache.camel.support.DefaultExchange;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Period;
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
    private JavaEmailService javaEmailService;
    @Autowired
    private CsvService csvService;
    @Autowired
    private AppointmentDetailRepo appointmentDetailRepo;
    @Autowired
    private ProducerTemplate producerTemplate;
    @Override
    public AppointmentDetailResponse getAppointmentDetail(Integer appointmentDetailId) throws GeneralException {
        Optional<AppointmentDetail> appointmentDetailById = appointmentDetailRepo.findById(appointmentDetailId);
        if(appointmentDetailById.isPresent()) {
            AppointmentDetail appointmentDetail = appointmentDetailById.get();
            AppointmentDetailResponse appointmentDetailResponse = modelMapper.map(appointmentDetail, AppointmentDetailResponse.class);
            String message = "Dear " + appointmentDetail.getFirstName() + " you have to pay total amount INR. " + appointmentDetail.getVaccine().getDiscountedPrice() +" Ignore if already paid.";
            appointmentDetailResponse.setMessage(message);
            return appointmentDetailResponse;
        } else {
            throw new GeneralException("No any appointment found with ID : "+appointmentDetailId);
        }
    }

    @Override
    public AppointmentDetailResponse bookAppointment(Integer slotId, Integer hospitalId, AppointmentDetailRequest appointmentDetailRequest) throws GeneralException, IOException {
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
                if(slot.getAvailableSlots() > 0) {
                    Person requiredDoctor = slot.getDoctor();
                    Vaccine requiredVaccine = slot.getVaccine();
                    Optional<Hospital> hospitalById = hospitalRepo.findById(hospitalId);
                    if(hospitalById.isPresent()) {
                        Hospital hospital = hospitalById.get();
                        if(hospital.getInventory() != null) {
                            if(hospital.getInventory().getAvailableVaccineCount() > 0 && !hospital.getInventory().getVaccines().isEmpty()) {
                                if(!hospital.getDoctors().isEmpty()) {
                                    List<Person> doctors = hospital.getDoctors();
                                    boolean doctorAvailabilityCheck = doctors.stream().anyMatch(doctor -> doctor.getPersonId() == requiredDoctor.getPersonId());
                                    if(doctorAvailabilityCheck) {
                                        List<Vaccine> vaccines = hospital.getInventory().getVaccines();
                                        boolean vaccineAvailabilityCheck = vaccines.stream().anyMatch(vaccine -> vaccine.getVaccineId() == requiredVaccine.getVaccineId());
                                        if(vaccineAvailabilityCheck) {
                                            Integer minAgeReq = requiredVaccine.getMinAge();
                                            Integer maxAgeReq = requiredVaccine.getMaxAge();
//                                            Integer currentAge = appointmentDetailRequest.getAge();
                                            LocalDate dateOfBirth = appointmentDetailRequest.getDateOfBirth();
                                            Period period = Period.between(dateOfBirth, currentDate);
                                            int ageInYears = period.getYears();
                                            if(ageInYears >= minAgeReq && ageInYears <= maxAgeReq) {
                                                AppointmentDetail appointmentDetail = modelMapper.map(appointmentDetailRequest, AppointmentDetail.class);
                                                appointmentDetail.setAge(ageInYears);
                                                appointmentDetail.setAppointmentDate(slotDate);
                                                appointmentDetail.setVaccine(requiredVaccine);
                                                appointmentDetail.setCreatedAt(LocalDateTime.now());
                                                appointmentDetail.setDoctor(requiredDoctor);
                                                String appointmentTime = slot.getStartTime() + " - " + slot.getEndTime();
                                                appointmentDetail.setAppointmentTime(appointmentTime);
                                                appointmentDetail.setHospital(hospital);
                                                appointmentDetail.setDoseNumber(appointmentDetailRequest.getDoseNumber());
                                                appointmentDetail.setSlot(slot);
                                                appointmentDetail.setVaccinated(false);
                                                Inventory inventory = hospital.getInventory();
                                                Integer availableVaccineCount = inventory.getAvailableVaccineCount();
                                                availableVaccineCount--;
                                                inventory.setAvailableVaccineCount(availableVaccineCount);
                                                Integer availableSlots = slot.getAvailableSlots();
                                                availableSlots--;
                                                slot.setAvailableSlots(availableSlots);
                                                slotRepo.save(slot);
                                                inventoryRepo.save(inventory);
                                                appointmentDetail = appointmentDetailRepo.save(appointmentDetail);
                                                hospital.getAppointmentDetails().add(appointmentDetail);
                                                hospitalRepo.save(hospital);
                                                requiredDoctor.getDoctorAppointmentDetails().add(appointmentDetail);
                                                personRepo.save(requiredDoctor);
                                                AppointmentDetailResponse appointmentDetailResponse = modelMapper.map(appointmentDetail, AppointmentDetailResponse.class);
                                                String firstName = appointmentDetailRequest.getFirstName();
                                                String gender = appointmentDetailRequest.getGender();
                                                String phone = appointmentDetailRequest.getPhone();
                                                String email = appointmentDetailRequest.getEmail();
                                                String message = "Dear " + firstName + ", your appointment has been booked. You have to pay total amount INR. " + requiredVaccine.getDiscountedPrice() +"."
                                                        + "\n\nPlease note appointmentId for future reference : "+appointmentDetail.getAppointmentDetailId();
                                                if(todayStartedButYetNotEnded) message = message + ". You are having very less time as your slotTime will end soon. Kindly make payment ASAP and take the vaccination.";

                                                AppointmentData appointmentData = new AppointmentData();
                                                appointmentData.setAppointmentDate(slotDate.toString());
                                                appointmentData.setAppointmentTime(appointmentTime);
                                                appointmentData.setNotified(false);
                                                appointmentData.setDoctorName(requiredDoctor.getFirstName() + " " + requiredDoctor.getLastName());
                                                appointmentData.setHospitalCity(hospital.getHospitalName());
                                                appointmentData.setHospitalContact(hospital.getAddress().getPhone());
                                                appointmentData.setHospitalName(hospital.getHospitalName());
                                                appointmentData.setPatientAge(ageInYears);
                                                appointmentData.setPatientEmail(email);
                                                appointmentData.setPatientName(firstName);
                                                appointmentData.setPatientGender(gender);
                                                appointmentData.setPatientPhone(phone);
                                                appointmentData.setVaccineName(requiredVaccine.getVaccineName());
                                                Exchange exchange = new DefaultExchange(new DefaultCamelContext());
                                                exchange.getIn().setBody(appointmentData);
                                                producerTemplate.send("seda:sendConfirmationEmail", exchange);
                                                appointmentDetailResponse.setMessage(message);

                                                return appointmentDetailResponse;
                                            } else {
                                                throw new GeneralException("You are not allowed to take this vaccine as your age : "+ageInYears+" is not in the range of : ( "+minAgeReq+" - "+maxAgeReq+" ).");
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
                    throw new GeneralException("All slots are booked no any slot available. Available slots : "+slot.getAvailableSlots()+". Try for another slot.");
                }
            } else {
                throw new GeneralException("Sorry slot is currently expired. slotDate : { "+slotDate+" }, startTime : { "+startTime+" }, endTime : { "+endTime+" }. Please try for another slot.");
            }
        } else {
            throw new GeneralException("Slot not found with ID : { "+slotId+" }. Enter correct ID.");
        }
    }

    @Override
    public AppointmentDetailResponse rescheduleAppointment(Integer newSlotId, Integer appointmentId) throws GeneralException, IOException {
        Optional<AppointmentDetail> appointmentDetailById = appointmentDetailRepo.findById(appointmentId);
        if(appointmentDetailById.isPresent()) {
            AppointmentDetail appointmentDetail = appointmentDetailById.get();
            Slot oldSlot = appointmentDetail.getSlot();
            Integer oldSlotCount = oldSlot.getAvailableSlots();
            if(!oldSlot.getSlotId().equals(newSlotId)) {
                Hospital hospital = appointmentDetail.getHospital();
                Optional<Slot> newSlotById = slotRepo.findById(newSlotId);
                if (newSlotById.isPresent()) {
                    Slot newSlot = newSlotById.get();
                    Person newSlotDoctor = newSlot.getDoctor();
                    Integer newSlotCount = newSlot.getAvailableSlots();
                    if(newSlotCount > 0) {
                        String newSlotVaccineName = newSlot.getVaccine().getVaccineName();
                        if(oldSlot.getVaccine().getVaccineName().equals(newSlotVaccineName)) {
                            newSlotCount--;
                            newSlot.setAvailableSlots(newSlotCount);
                            oldSlotCount++;
                            oldSlot.setAvailableSlots(oldSlotCount);
                            appointmentDetail.setSlot(newSlot);
                            appointmentDetail.setDoctor(newSlotDoctor);
                            String newSlotAppointmentTime = newSlot.getStartTime() + " - " + newSlot.getEndTime();
                            appointmentDetail.setAppointmentTime(newSlotAppointmentTime);
                            LocalDate newSlotSlotDate = newSlot.getSlotDate();
                            appointmentDetail.setAppointmentDate(newSlotSlotDate);
                            appointmentDetail.setCreatedAt(LocalDateTime.now());
                            appointmentDetail = appointmentDetailRepo.save(appointmentDetail);
                            slotRepo.save(newSlot);
                            slotRepo.save(oldSlot);
                            AppointmentDetailResponse appointmentDetailResponse = modelMapper.map(appointmentDetail, AppointmentDetailResponse.class);
                            String firstName = appointmentDetail.getFirstName();
                            String gender = appointmentDetail.getGender();
                            Integer age = appointmentDetail.getAge();
                            String phone = appointmentDetail.getPhone();
                            String email = appointmentDetail.getEmail();
                            String message = "Dear " + firstName + ", your appointment has been rescheduled. Kindle make payment if not done already.";
                            appointmentDetailResponse.setMessage(message);

                            AppointmentData appointmentData = new AppointmentData();
                            appointmentData.setAppointmentDate(newSlotSlotDate.toString());
                            appointmentData.setAppointmentTime(newSlotAppointmentTime);
                            appointmentData.setNotified(true);
                            appointmentData.setDoctorName(newSlotDoctor.getFirstName() + " " + newSlotDoctor.getLastName());
                            appointmentData.setHospitalCity(hospital.getHospitalName());
                            appointmentData.setHospitalContact(hospital.getAddress().getPhone());
                            appointmentData.setHospitalName(hospital.getHospitalName());
                            appointmentData.setPatientAge(age);
                            appointmentData.setPatientEmail(email);
                            appointmentData.setPatientName(firstName);
                            appointmentData.setPatientGender(gender);
                            appointmentData.setPatientPhone(phone);
                            appointmentData.setVaccineName(newSlotVaccineName);
                            Exchange exchange = new DefaultExchange(new DefaultCamelContext());
                            exchange.getIn().setBody(appointmentData);
                            producerTemplate.send("seda:sendRescheduleEmail", exchange);

                            return appointmentDetailResponse;
                        } else {
                            throw new GeneralException("Selected slot does not have required vaccine : "+oldSlot.getVaccine().getVaccineName());
                        }
                    } else {
                        throw new GeneralException("All slots are booked no any slot available. Available slots : "+newSlot.getAvailableSlots()+". Try for another slot.");
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
