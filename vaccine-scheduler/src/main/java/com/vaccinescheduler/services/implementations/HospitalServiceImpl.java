package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.dtos.request.DoctorListRrquest;
import com.vaccinescheduler.dtos.request.HospitalRequest;
import com.vaccinescheduler.dtos.response.AppointmentResponse;
import com.vaccinescheduler.dtos.response.HospitalResponse;
import com.vaccinescheduler.dtos.response.PaymentDetailResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.*;
import com.vaccinescheduler.repositories.HospitalRepo;
import com.vaccinescheduler.repositories.InventoryRepo;
import com.vaccinescheduler.repositories.PersonRepo;
import com.vaccinescheduler.services.HospitalService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HospitalServiceImpl implements HospitalService {
    @Autowired
    private HospitalRepo hospitalRepo;
    @Autowired
    private InventoryRepo inventoryRepo;
    @Autowired
    private PersonRepo personRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public HospitalResponse createHospital(HospitalRequest hospitalRequest) throws GeneralException {
        Hospital hospital = modelMapper.map(hospitalRequest, Hospital.class);
        hospital = hospitalRepo.save(hospital);
        HospitalResponse hospitalResponse = modelMapper.map(hospital, HospitalResponse.class);
        return hospitalResponse;
    }

    @Override
    public HospitalResponse getHospital(Integer hospitalId) throws GeneralException {
        Optional<Hospital> hospitalById = hospitalRepo.findById(hospitalId);
        if(hospitalById.isPresent()) {
            Hospital hospital = hospitalById.get();
            HospitalResponse hospitalResponse = modelMapper.map(hospital, HospitalResponse.class);
            return hospitalResponse;
        } else {
            throw new GeneralException("Hospital not found with ID : "+hospitalId);
        }
    }

    @Override
    public HospitalResponse updateHospital(Integer hospitalId, HospitalRequest hospitalRequest) throws GeneralException {
        Optional<Hospital> hospitalById = hospitalRepo.findById(hospitalId);
        if(hospitalById.isPresent()) {
            Hospital oldHospital = hospitalById.get();
            Hospital updatedHospital = modelMapper.map(hospitalRequest, Hospital.class);
            if(updatedHospital.getHospitalName() != null) {
                oldHospital.setHospitalName(updatedHospital.getHospitalName());
            }
            if(updatedHospital.getHospitalType() != null) {
                oldHospital.setHospitalType(updatedHospital.getHospitalType());
            }
            if(updatedHospital.getHospitalPinCode() != null) {
                oldHospital.setHospitalPinCode(updatedHospital.getHospitalPinCode());
            }
            if(updatedHospital.getOperatingHours() != null) {
                oldHospital.setOperatingHours(updatedHospital.getOperatingHours());
            }
            if(updatedHospital.getAddress() != null) {
                if(updatedHospital.getAddress().getPhone() != null) {
                    oldHospital.getAddress().setPhone(updatedHospital.getAddress().getPhone());
                }
                if(updatedHospital.getAddress().getCity() != null) {
                    oldHospital.getAddress().setCity(updatedHospital.getAddress().getCity());
                }
                if(updatedHospital.getAddress().getEmail() != null) {
                    oldHospital.getAddress().setEmail(updatedHospital.getAddress().getEmail());
                }
            }
            oldHospital = hospitalRepo.save(oldHospital);
            HospitalResponse hospitalResponse = modelMapper.map(oldHospital, HospitalResponse.class);
            return hospitalResponse;
        } else {
            throw new GeneralException("Hospital not found with ID : "+hospitalId);
        }
    }

    @Override
    public Boolean deleteHospital(Integer hospitalId) throws GeneralException {
        Optional<Hospital> hospitalById = hospitalRepo.findById(hospitalId);
        if(hospitalById.isPresent()) {
            Hospital hospital = hospitalById.get();
            hospitalRepo.delete(hospital);
            return true;
        } else {
            throw new GeneralException("Hospital not found with ID : "+hospitalId);
        }
    }

    @Override
    public String addInventoryToHospital(Integer hospitalId, Integer inventoryId) throws GeneralException {
        Optional<Hospital> hospitalById = hospitalRepo.findById(hospitalId);
        if(hospitalById.isPresent()) {
            Hospital hospital = hospitalById.get();
            Optional<Inventory> inventoryById = inventoryRepo.findById(inventoryId);
            if(inventoryById.isPresent()) {
                Inventory inventory = inventoryById.get();
                if(hospital.getInventory() == null) {
                    hospital.setInventory(inventory);
                    hospitalRepo.save(hospital);
                    return "Inventory with ID : { "+inventoryId+" } assigned to hospital with ID : { "+hospitalId+" }";
                } else {
                    throw new GeneralException("Inventory already registered to hospital.");
                }
            } else {
                throw new GeneralException("Inventory not found with ID : "+inventoryId);
            }
        } else {
            throw new GeneralException("Hospital not found with ID : "+hospitalId);
        }
    }

    @Override
    public List<PaymentDetailResponse> getAllPayments(Integer hospitalId) throws GeneralException {
        Optional<Hospital> hospitalById = hospitalRepo.findById(hospitalId);
        if(hospitalById.isPresent()) {
            Hospital hospital = hospitalById.get();
            if(!hospital.getPaymentDetails().isEmpty()) {
                List<PaymentDetail> paymentDetails = hospital.getPaymentDetails();
                List<PaymentDetailResponse> paymentDetailResponses = new ArrayList<>();
                for(PaymentDetail paymentDetail : paymentDetails) {
                    PaymentDetailResponse paymentDetailResponse = modelMapper.map(paymentDetail, PaymentDetailResponse.class);
                    paymentDetailResponses.add(paymentDetailResponse);
                }
                return paymentDetailResponses;
            } else {
                throw new GeneralException("No any payment information available in hospital records.");
            }
        } else {
            throw new GeneralException("Hospital not found with ID : "+hospitalId);
        }
    }

    @Override
    public List<AppointmentResponse> getAllAppointments(Integer hospitalId) throws GeneralException {
        Optional<Hospital> hospitalById = hospitalRepo.findById(hospitalId);
        if(hospitalById.isPresent()) {
            Hospital hospital = hospitalById.get();
            if(!hospital.getAppointmentDetails().isEmpty()) {
                List<AppointmentDetail> appointmentDetails = hospital.getAppointmentDetails();
                List<AppointmentResponse> appointmentResponses = new ArrayList<>();
                for(AppointmentDetail appointmentDetail : appointmentDetails) {
                    AppointmentResponse appointmentResponse = modelMapper.map(appointmentDetail, AppointmentResponse.class);
                    appointmentResponses.add(appointmentResponse);
                }
                return appointmentResponses;
            } else {
                throw new GeneralException("No any appointment information available in hospital records.");
            }
        } else {
            throw new GeneralException("Hospital not found with ID : "+hospitalId);
        }
    }

    @Override
    public String addDoctorsToHospital(DoctorListRrquest doctorListRrquest) throws GeneralException {
        Integer hospitalId = doctorListRrquest.getHospitalId();
        Optional<Hospital> hospitalById = hospitalRepo.findById(hospitalId);
        if(hospitalById.isPresent()) {
            Hospital hospital = hospitalById.get();
            List<Integer> doctorIds = doctorListRrquest.getDoctorIds();
            Boolean validDoctorCheck = false;
            StringBuilder validDoctorResult = new StringBuilder();
            Boolean invalidDoctorCheck = false;
            StringBuilder invalidDoctorResult = new StringBuilder();
            List<Person> doctors = new ArrayList<>();
            for(Integer doctorId : doctorIds) {
                Optional<Person> doctorById = personRepo.findById(doctorId);
                if(doctorById.isPresent()) {
                    Person doctor = doctorById.get();
                    if(doctor.getRole().toLowerCase().endsWith("doctor")) {
                        if(doctor.getHospital() == null) {
                            validDoctorCheck = true;
                            validDoctorResult.append(doctorId+" ");
                            doctors.add(doctor);
                            doctor.setHospital(hospital);
                            personRepo.save(doctor);
                        }
                    } else {
                        invalidDoctorCheck = true;
                        invalidDoctorResult.append(doctorId+" ");
                    }
                }
            }
            if(validDoctorCheck && invalidDoctorCheck) {
                hospital.getDoctors().addAll(doctors);
                hospitalRepo.save(hospital);
                return validDoctorResult+"} : Some doctors registered to hospital. Found some invalid doctor ids : { "+invalidDoctorResult+" }";
            } else if(validDoctorCheck) {
                hospital.getDoctors().addAll(doctors);
                hospitalRepo.save(hospital);
                return validDoctorResult+" } : doctors registered to hospital.";
            } else {
                return "Invalid doctor ids : { "+invalidDoctorResult+" }";
            }
        } else {
            throw new GeneralException("Hospital not found with ID : "+hospitalId);
        }
    }
}
