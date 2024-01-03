package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.dtos.other.VaccinationData;
import com.vaccinescheduler.dtos.request.AppointmentDetailRequest;
import com.vaccinescheduler.dtos.request.PaymentDetailRequest;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.*;
import com.vaccinescheduler.services.CsvService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import com.opencsv.CSVWriter;
import org.springframework.stereotype.Service;

@Service
public class CsvServiceImpl implements CsvService {
    private final Logger LOGGER = LoggerFactory.getLogger(CsvServiceImpl.class);
    @Override
    public String bookingDataToCSV(AppointmentDetailRequest appointmentDetailRequest, Slot slot, Hospital hospital, Person requiredDoctor, Vaccine requiredVaccine) throws IOException {
        String patientName = appointmentDetailRequest.getFirstName();
        LocalDate appointmentDate = slot.getSlotDate();
        String appointmentTime = slot.getStartTime() + " - " + slot.getEndTime();
        Integer patientAge = appointmentDetailRequest.getAge();
        String patientGender = appointmentDetailRequest.getGender();
        String patientPhone = appointmentDetailRequest.getPhone();
        String patientEmail = appointmentDetailRequest.getEmail();
        String doctorFirstName = requiredDoctor.getFirstName();
        String doctorLastName = requiredDoctor.getLastName();
        String hospitalName = hospital.getHospitalName();
        String vaccineName = requiredVaccine.getVaccineName();
        String hospitalCity = hospital.getAddress().getCity();
        String hospitalPhone = hospital.getAddress().getPhone();

        String csvFileHeader = "patientName, patientAge, patientGender ,patientPhone, patientEmail,HospitalName,HospitalCity,HospitalContact,VaccineName,DoctorName,AppointmentDate,AppointmentTime,notified";
        String filePath = "src/main/resources/csv/appointmentDetails.csv";
        File file = new File(filePath);
        if(!file.exists()) {
            boolean newFile = file.createNewFile();
            LOGGER.info("File created at location : src/main/resources/csv/appointmentDetails.csv");
            String[] headers = csvFileHeader.split(",");
            try(CSVWriter csvWriter = new CSVWriter(new FileWriter(file, true))) {
                csvWriter.writeNext(headers);
                String[] values = new String[] {patientName, String.valueOf(patientAge), patientGender, patientPhone, patientEmail, hospitalName, hospitalCity, hospitalPhone, vaccineName, doctorFirstName + " " + doctorLastName, String.valueOf(appointmentDate), appointmentTime, String.valueOf(false)};
                csvWriter.writeNext(values);
                LOGGER.info("File created & data added to csv file.");
            }
        } else {
            try(CSVWriter csvWriter = new CSVWriter(new FileWriter(file, true))) {
                String[] values = new String[] {patientName, String.valueOf(patientAge), patientGender, patientPhone, patientEmail, hospitalName, hospitalCity, hospitalPhone, vaccineName, doctorFirstName + " " + doctorLastName, String.valueOf(appointmentDate), appointmentTime, String.valueOf(false)};
                csvWriter.writeNext(values);
                LOGGER.info("Data added to csv file appointmentDetails.csv");
            }
        }
        return "Data added to csv file appointmentDetails.";
    }

    @Override
    public String reschedulingDataToCSV(AppointmentDetail appointmentDetail, Slot slot, Hospital hospital, Person requiredDoctor, String vaccineName) throws IOException {
        String patientName = appointmentDetail.getFirstName();
        LocalDate appointmentDate = slot.getSlotDate();
        String appointmentTime = slot.getStartTime() + " - " + slot.getEndTime();
        Integer patientAge = appointmentDetail.getAge();
        String patientGender = appointmentDetail.getGender();
        String patientPhone = appointmentDetail.getPhone();
        String patientEmail = appointmentDetail.getEmail();
        String doctorFirstName = requiredDoctor.getFirstName();
        String doctorLastName = requiredDoctor.getLastName();
        String hospitalName = hospital.getHospitalName();
        String hospitalCity = hospital.getAddress().getCity();
        String hospitalPhone = hospital.getAddress().getPhone();

        String csvFileHeader = "patientName, patientAge, patientGender ,patientPhone, patientEmail,HospitalName,HospitalCity,HospitalContact,VaccineName,DoctorName,AppointmentDate,AppointmentTime,notified";
        String filePath = "src/main/resources/csv/rescheduleDetails.csv";
        File file = new File(filePath);
        if(!file.exists()) {
            boolean newFile = file.createNewFile();
            LOGGER.info("File created at location : src/main/resources/csv/rescheduleDetails.csv");
            String[] headers = csvFileHeader.split(",");
            try(CSVWriter csvWriter = new CSVWriter(new FileWriter(file, true))) {
                csvWriter.writeNext(headers);
                String[] values = new String[] {patientName, String.valueOf(patientAge), patientGender, patientPhone, patientEmail, hospitalName, hospitalCity, hospitalPhone, vaccineName, doctorFirstName + " " + doctorLastName, String.valueOf(appointmentDate), appointmentTime, String.valueOf(false)};
                csvWriter.writeNext(values);
                LOGGER.info("File created & data added to csv file rescheduleDetails.");
            }
        } else {
            try(CSVWriter csvWriter = new CSVWriter(new FileWriter(file, true))) {
                String[] values = new String[] {patientName, String.valueOf(patientAge), patientGender, patientPhone, patientEmail, hospitalName, hospitalCity, hospitalPhone, vaccineName, doctorFirstName + " " + doctorLastName, String.valueOf(appointmentDate), appointmentTime, String.valueOf(false)};
                csvWriter.writeNext(values);
                LOGGER.info("Data added to csv file rescheduleDetails.csv");
            }
        }
        return "Data added to csv file rescheduleDetails.";
    }

    @Override
    public String vaccinatedDataToCSV(VaccinationData vaccinationData) throws GeneralException {
        String formatDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        String filePath = "src/main/resources/csv/vaccinationData_"+formatDate+".csv";
        File file = new File(filePath);
        if(!file.exists()) {
            String csvFileHeaders = "patientId,patientName,patientAadhaarNumber,patientEmail,patientPhone,vaccinationDetailId,vaccinatedDate,vaccinatedTime,vaccineName,vaccineDoseNumber,nextVaccinationDate,hospitalName";
            String [] headers = csvFileHeaders.split(",");
            try(CSVWriter csvWriter = new CSVWriter(new FileWriter(file, true))) {
                csvWriter.writeNext(headers);
                Integer patientId = vaccinationData.getPatientId();
                Integer vaccinationDetailId = vaccinationData.getVaccinationDetailId();
                String patientName = vaccinationData.getPatientName();
                String patientAadhaarNumber = vaccinationData.getPatientAadhaarNumber();
                String patientEmail = vaccinationData.getPatientEmail();
                String patientPhone = vaccinationData.getPatientPhone();
                LocalDate vaccinatedDate = vaccinationData.getVaccinatedDate();
                String vaccinatedTime = vaccinationData.getVaccinatedTime();
                String vaccineName = vaccinationData.getVaccineName();
                String vaccineDoseNumber = vaccinationData.getVaccineDoseNumber();
                LocalDate nextVaccinationDate = vaccinationData.getNextVaccinationDate();
                String hospitalName = vaccinationData.getHospitalName();
                String[] values = new String[] {String.valueOf(patientId),patientName,patientAadhaarNumber,patientEmail,patientPhone,String.valueOf(vaccinationDetailId),String.valueOf(vaccinatedDate),vaccinatedTime,vaccineName,vaccineDoseNumber,String.valueOf(nextVaccinationDate),hospitalName};
                csvWriter.writeNext(values);
                LOGGER.info("File created and Data written to file vaccinationData.csv");
            } catch (IOException e) {
                throw new GeneralException(e.getMessage());
            }
        } else {
            try(CSVWriter csvWriter = new CSVWriter(new FileWriter(file, true))) {
                Integer patientId = vaccinationData.getPatientId();
                Integer vaccinationDetailId = vaccinationData.getVaccinationDetailId();
                String patientName = vaccinationData.getPatientName();
                String patientAadhaarNumber = vaccinationData.getPatientAadhaarNumber();
                String patientEmail = vaccinationData.getPatientEmail();
                String patientPhone = vaccinationData.getPatientPhone();
                LocalDate vaccinatedDate = vaccinationData.getVaccinatedDate();
                String vaccinatedTime = vaccinationData.getVaccinatedTime();
                String vaccineName = vaccinationData.getVaccineName();
                String vaccineDoseNumber = vaccinationData.getVaccineDoseNumber();
                LocalDate nextVaccinationDate = vaccinationData.getNextVaccinationDate();
                String hospitalName = vaccinationData.getHospitalName();
                String[] values = new String[] {String.valueOf(patientId),patientName,patientAadhaarNumber,patientEmail,patientPhone,String.valueOf(vaccinationDetailId),String.valueOf(vaccinatedDate),vaccinatedTime,vaccineName,vaccineDoseNumber,String.valueOf(nextVaccinationDate),hospitalName};
                csvWriter.writeNext(values);
                LOGGER.info("File present and Data written to file vaccinationData.csv");
            } catch (IOException e) {
                throw new GeneralException(e.getMessage());
            }
        }
        return "Date written to csv file.";
    }

    @Override
    public String paymentDataToCSV(PaymentDetailRequest paymentDetailRequest, Person patient, Hospital hospital) throws IOException {
        String patientName = patient.getFirstName() + " " + patient.getLastName();
        String patientGender = patient.getGender();
        Integer patientAge = patient.getAge();
        String patientCity = patient.getAddress().getCity();
        String patientPhone = patient.getAddress().getPhone();
        String patientEmail = patient.getAddress().getEmail();
        String hospitalName = hospital.getHospitalName();
        Double paidAmount = paymentDetailRequest.getAmount();
        String paymentMethod = paymentDetailRequest.getPaymentMethod();

        String csvFileHeader = "patientName,patientGender,patientAge,patientCity,patientPhone,patientEmail,hospitalName,paidAmount,paymentMethod,notified";
        String filePath = "src/main/resources/csv/paymentDetails.csv";
        File file = new File(filePath);
        if(!file.exists()) {
            boolean newFile = file.createNewFile();
            LOGGER.info("File created at : src/main/resources/csv/paymentDetails.csv");
            try(CSVWriter csvWriter = new CSVWriter(new FileWriter(file, true))) {
                String[] headers = csvFileHeader.split(",");
                csvWriter.writeNext(headers);
                String[] values = new String[]{patientName ,patientGender ,String.valueOf(patientAge) ,patientCity ,patientPhone ,patientEmail ,hospitalName ,String.valueOf(paidAmount) ,paymentMethod, String.valueOf(false)};
                csvWriter.writeNext(values);
                LOGGER.info("File created and data added to paymentDetails.csv");
            }
        } else {
            try(CSVWriter csvWriter = new CSVWriter(new FileWriter(file, true))) {
                String[] values = new String[]{patientName, patientGender, String.valueOf(patientAge), patientCity, patientPhone, patientEmail, hospitalName, String.valueOf(paidAmount), paymentMethod, String.valueOf(false)};
                csvWriter.writeNext(values);
                LOGGER.info("Data added to paymentDetails.csv");
            }
        }
        return "Data added to csv file paymentDetails.";
    }
}
