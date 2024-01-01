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
    private JavaEmailService javaEmailService;
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
                                            Integer currentAge = appointmentDetailRequest.getAge();
                                            if(currentAge >= minAgeReq && currentAge <= maxAgeReq) {
                                                AppointmentDetail appointmentDetail = modelMapper.map(appointmentDetailRequest, AppointmentDetail.class);
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
                                                String message = "Dear " + firstName + ", your appointment has been booked. "
                                                        + "We look forward to providing you with excellent service. "
                                                        + "Details: Gender - " + gender + ", Age - " + currentAge + ", Phone - " + phone + ", Email - " + email;
                                                if(todayStartedButYetNotEnded) message = message + ". You are having very less time as your slotTime will end soon. Kindly make payment ASAP and take the vaccination.";
                                                StringBuilder emailMessage = new StringBuilder();
                                                emailMessage.append("Dear ").append(firstName).append("\n")
                                                        .append("\n\nWe're excited to confirm your upcoming vaccination appointment at '").append(hospital.getHospitalName()).append("'.\n\nHere are the details:\n")
                                                        .append("Appointment Date: ").append(slotDate).append("\n")
                                                        .append("Appointment Time: ").append(appointmentTime).append("\n")
                                                        .append("Doctor: Dr. ").append(requiredDoctor.getFirstName()).append(" ").append(requiredDoctor.getLastName()).append("\n")
                                                        .append("Vaccine: ").append(requiredVaccine.getVaccineName()).append("\n\n")
                                                        .append("Patient Details:\n")
                                                        .append("Name : ").append(firstName).append("\n")
                                                        .append("Age : ").append(currentAge).append("\n")
                                                        .append("Gender : ").append(gender).append("\n")
                                                        .append("Phone : ").append(phone).append("\n")
                                                        .append("Email : ").append(email).append("\n\n")
                                                        .append("Hospital Details:\n")
                                                        .append("Name : ").append(hospital.getHospitalName()).append("\n")
                                                        .append("Address : ").append(hospital.getAddress().getCity()).append("\n")
                                                        .append("Contact : ").append(hospital.getAddress().getPhone()).append("\n\n")
                                                        .append("Please arrive a little early and remember to bring any required documents. If you have any questions or need to reschedule, feel free to contact us.\n\n")
                                                        .append("We appreciate your trust in '").append(hospital.getHospitalName()).append("' and look forward to providing you with excellent care.\n\n")
                                                        .append("Best regards,\n")
                                                        .append(hospital.getHospitalName()+".");
                                                javaEmailService.sendEmail(email, "Appointment confirmation from ~ [ "+hospital.getHospitalName()+" ]", emailMessage.toString());
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
    public AppointmentDetailResponse rescheduleAppointment(Integer newSlotId, Integer appointmentId) throws GeneralException {
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
                        String newSlotvaccineName = newSlot.getVaccine().getVaccineName();
                        if(oldSlot.getVaccine().getVaccineName().equals(newSlotvaccineName)) {
                            newSlotCount--;
                            newSlot.setAvailableSlots(newSlotCount);
                            oldSlotCount++;
                            oldSlot.setAvailableSlots(oldSlotCount);
                            appointmentDetail.setSlot(newSlot);
                            appointmentDetail.setDoctor(newSlotDoctor);
                            String newSlotappointmentTime = newSlot.getStartTime() + " - " + newSlot.getEndTime();
                            appointmentDetail.setAppointmentTime(newSlotappointmentTime);
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
                            String message = "Dear " + firstName + ", your appointment has been rescheduled. Kindle make payment if not done already."
                                    + "Details: Gender - " + gender + ", Age - " + age + ", Phone - " + phone + ", Email - " + email;
                            appointmentDetailResponse.setMessage(message);
                            StringBuilder emailMessage = new StringBuilder();
                            emailMessage.append("Dear " + firstName + ",\n\n")
                            .append("We hope this message finds you well. We want to inform you that your vaccination appointment at '"
                                    + hospital.getHospitalName() + "' has been successfully rescheduled. \nHere are the updated details:\n\n")
                            .append("- New Appointment Date: " + newSlotSlotDate + "\n")
                            .append("- New Appointment Time: " + newSlotappointmentTime + "\n")
                            .append("- Doctor: Dr. " + newSlotDoctor.getFirstName() + " " + newSlotDoctor.getLastName() + "\n")
                            .append("- Vaccine: " + newSlotvaccineName + "\n\n")
                            .append("Patient Details:\n")
                            .append("- Name: " + firstName + "\n")
                            .append("- Age: " + age + "\n")
                            .append("- Gender: " + gender + "\n")
                            .append("- Phone: " + phone + "\n")
                            .append("- Email: " + email + "\n\n")
                            .append("Hospital Details:\n")
                            .append("- Name: " + hospital.getHospitalName() + "\n")
                            .append("- Address: " + hospital.getAddress().getCity() + "\n")
                            .append("- Contact: " + hospital.getAddress().getPhone() + "\n\n")
                            .append("Please arrive a little early, and if you have any concerns or questions about the rescheduled appointment, feel free to contact us.\n\n")
                            .append("We appreciate your flexibility and understanding. Thank you for choosing '" + hospital.getHospitalName() + "'.\n\n")
                            .append("Best regards,\n")
                            .append(hospital.getHospitalName()+".");
                            javaEmailService.sendEmail(appointmentDetail.getEmail(), "Rescheduled appointment confirmation from ~ [ "+hospital.getHospitalName()+" ]", emailMessage.toString());
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
