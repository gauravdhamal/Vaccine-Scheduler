package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.dtos.other.VaccinationData;
import com.vaccinescheduler.dtos.response.VaccinationResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.*;
import com.vaccinescheduler.repositories.AppointmentDetailRepo;
import com.vaccinescheduler.repositories.VaccinationDetailRepo;
import com.vaccinescheduler.services.CsvService;
import com.vaccinescheduler.services.VaccinationDetailService;
import org.apache.camel.Exchange;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.support.DefaultExchange;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class VaccinationDetailServiceImpl implements VaccinationDetailService {
    @Autowired
    private VaccinationDetailRepo vaccinationDetailRepo;
    @Autowired
    private AppointmentDetailRepo appointmentDetailRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private JavaEmailService javaEmailService;
    @Autowired
    private ProducerTemplate producerTemplate;
    @Autowired
    private CsvService csvService;
    @Override
    public List<VaccinationResponse> getVaccinationDetailsByDateAndSlot(LocalDate date, String slot) throws GeneralException {
        LocalTime slotTime;
        if(slot.equals("morning")) {
            slotTime = LocalTime.of(12,0,0);
        } else if(slot.equals("afternoon")) {
            slotTime = LocalTime.of(16,0,0);
        } else if(slot.equals("evening")) {
            slotTime = LocalTime.of(19,0,0);
        } else {
            throw new GeneralException("Invalid slot selected : "+slot+". Slot must be either 'morning', 'afternoon' or 'evening'.");
        }
        Optional<List<VaccinationDetail>> vaccinationDetailsByVaccinatedDateAndVaccinatedTime = vaccinationDetailRepo.findByVaccinatedDateAndVaccinatedTime(date, slotTime);
        if(vaccinationDetailsByVaccinatedDateAndVaccinatedTime.isPresent() && !vaccinationDetailsByVaccinatedDateAndVaccinatedTime.get().isEmpty()) {
            List<VaccinationDetail> vaccinationDetails = vaccinationDetailsByVaccinatedDateAndVaccinatedTime.get();
            List<VaccinationResponse> vaccinationResponses = new ArrayList<>();
            for(VaccinationDetail vaccinationDetail : vaccinationDetails) {
                VaccinationResponse vaccinationResponse = modelMapper.map(vaccinationDetail, VaccinationResponse.class);
                vaccinationResponses.add(vaccinationResponse);
            }
            return vaccinationResponses;
        } else {
            throw new GeneralException("No any patients found for date : { "+date+" }, slot : { "+slot+" }.");
        }
    }

    @Override
    public List<VaccinationResponse> updateVaccinationRecord() throws GeneralException {
        LocalTime currentTime = LocalTime.now();
        LocalTime morningSlotTime = LocalTime.of(12, 0,30);
        LocalTime afternoonSlotTime = LocalTime.of(16, 0,30);
        LocalTime eveningSlotTime = LocalTime.of(19, 0,30);
        String slotTime;
        if(currentTime.isAfter(eveningSlotTime)) {
            slotTime = "17:00 - 19:00";
        } else if(currentTime.isAfter(afternoonSlotTime)) {
            slotTime = "13:00 - 16:00";
        } else {
            slotTime = "09:00 - 12:00";
        }
        LocalDate currentDate = LocalDate.now();
        Boolean vaccinated = false;
        Optional<List<AppointmentDetail>> appointmentDetailsByAppointmentDate = appointmentDetailRepo.findPastAppointments(currentDate, slotTime, vaccinated);
        if(appointmentDetailsByAppointmentDate.isPresent() && !appointmentDetailsByAppointmentDate.get().isEmpty()) {
            List<AppointmentDetail> appointmentDetails = appointmentDetailsByAppointmentDate.get();
            List<VaccinationResponse> vaccinationResponses = new ArrayList<>();
            for(AppointmentDetail appointmentDetail : appointmentDetails) {
                appointmentDetail.setVaccinated(true);
                VaccinationDetail vaccinationDetail = new VaccinationDetail();
                Vaccine vaccine = appointmentDetail.getVaccine();
                String appointmentTime = appointmentDetail.getAppointmentTime();
                vaccinationDetail.setVaccine(vaccine);
                Person doctor = appointmentDetail.getDoctor();
                vaccinationDetail.setDoctor(doctor);
                Hospital hospital = appointmentDetail.getHospital();
                vaccinationDetail.setHospital(hospital);
                Person patient = appointmentDetail.getPatient();
                vaccinationDetail.setPatient(patient);
                String doseNumber = appointmentDetail.getDoseNumber();
                vaccinationDetail.setDoseNumber(doseNumber);
                vaccinationDetail.setVaccinationStatus(appointmentDetail.getVaccinated());
                LocalDate appointmentDate = appointmentDetail.getAppointmentDate();
                vaccinationDetail.setVaccinatedDate(appointmentDate);
                if(appointmentTime.endsWith("12:00")) {
                    vaccinationDetail.setVaccinatedTime(LocalTime.of(12, 00, 00));
                } else if(appointmentTime.endsWith("16:00")) {
                    vaccinationDetail.setVaccinatedTime(LocalTime.of(16, 00,00));
                } else {
                    vaccinationDetail.setVaccinatedTime(LocalTime.of(19, 00, 00));
                }
                Integer daysBetweenDoses = vaccine.getDaysBetweenDoses();
                LocalDate nextVaccinationDate = appointmentDate.plusDays(daysBetweenDoses);
                vaccinationDetail.setNextVaccinationDate(nextVaccinationDate);
                vaccinationDetail = vaccinationDetailRepo.save(vaccinationDetail);
                appointmentDetailRepo.save(appointmentDetail);
                VaccinationResponse vaccinationResponse = modelMapper.map(vaccinationDetail, VaccinationResponse.class);
                vaccinationResponses.add(vaccinationResponse);

                VaccinationData vaccinationData = new VaccinationData();
                vaccinationData.setVaccinationDetailId(vaccinationDetail.getVaccinationDetailId());
                vaccinationData.setPatientId(patient.getPersonId());
                vaccinationData.setPatientName(patient.getFirstName());
                vaccinationData.setPatientAadhaarNumber(patient.getAadhaarNumber());
                vaccinationData.setPatientPhone(patient.getAddress().getPhone());
                vaccinationData.setPatientEmail(patient.getAddress().getEmail());
                vaccinationData.setHospitalName(hospital.getHospitalName());
                vaccinationData.setVaccinatedDate(appointmentDate);
                vaccinationData.setVaccinatedTime(appointmentTime);
                vaccinationData.setVaccineName(vaccine.getVaccineName());
                vaccinationData.setVaccineDoseNumber(doseNumber);
                vaccinationData.setNextVaccinationDate(nextVaccinationDate);
                Exchange exchange = new DefaultExchange(new DefaultCamelContext());
                exchange.getIn().setBody(vaccinationData);
                producerTemplate.send("seda:updateVaccinationRecord", exchange);

                csvService.vaccinatedDataToCSV(vaccinationData);
            }
            return vaccinationResponses;
        } else {
            throw new GeneralException("No any appointment found past the current time.");
        }
    }

    @Override
    public List<VaccinationResponse> getAllVaccinationRecords() throws GeneralException {
        List<VaccinationDetail> vaccinationDetailList = vaccinationDetailRepo.findAll();
        if(!vaccinationDetailList.isEmpty()) {
            List<VaccinationResponse> vaccinationResponses = new ArrayList<>();
            for(VaccinationDetail vaccinationDetail : vaccinationDetailList) {
                VaccinationResponse vaccinationResponse = modelMapper.map(vaccinationDetail, VaccinationResponse.class);
                vaccinationResponses.add(vaccinationResponse);
            }
            return vaccinationResponses;
        } else {
            throw new GeneralException("No any vaccination record found in database.");
        }
    }
}
