package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.dtos.request.AddSlotsRequest;
import com.vaccinescheduler.dtos.response.AppointmentResponse;
import com.vaccinescheduler.dtos.response.HospitalResponse;
import com.vaccinescheduler.dtos.response.PersonResponse;
import com.vaccinescheduler.dtos.response.SlotResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.*;
import com.vaccinescheduler.repositories.PersonRepo;
import com.vaccinescheduler.repositories.SlotRepo;
import com.vaccinescheduler.services.DoctorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {
    @Autowired
    private PersonRepo personRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private SlotRepo slotRepo;
    @Override
    public HospitalResponse getAssignedHospitalByDoctorId(Integer doctorId) throws GeneralException {
        Optional<Person> doctorById = personRepo.findById(doctorId);
        if(doctorById.isPresent()) {
            Person doctor = doctorById.get();
            if(doctor.getRole().toLowerCase().endsWith("doctor")) {
                if(doctor.getHospital() != null) {
                    Hospital hospital = doctor.getHospital();
                    HospitalResponse hospitalResponse = modelMapper.map(hospital, HospitalResponse.class);
                    List<Person> doctors = hospital.getDoctors();
                    List<String> doctorInfo = (doctors.stream().map(tempDoctor -> "Id : '"+tempDoctor.getPersonId() + "' , Username : '" + tempDoctor.getUsername() + "' , Slots : '" + tempDoctor.getSlots().size()+"'").collect(Collectors.toList()));
                    hospitalResponse.setDoctorDetails(doctorInfo);
                    return hospitalResponse;
                } else {
                    throw new GeneralException("Doctor with ID { "+doctorId+" } is not associated with any hospital.");
                }
            } else {
                throw new GeneralException("Username : { "+doctor.getUsername()+" } is a { "+doctor.getRole()+" }. Person must be doctor. Enter correct ID.");
            }
        } else {
            throw new GeneralException("Doctor not found with ID : "+doctorId);
        }
    }

    @Override
    public String addSlotsByDoctorId(AddSlotsRequest addSlotsRequest) throws GeneralException {
        Integer doctorId = addSlotsRequest.getDoctorId();
        Optional<Person> doctorById = personRepo.findById(doctorId);
        if(doctorById.isPresent()) {
            Person doctor = doctorById.get();
            if(doctor.getRole().toLowerCase().endsWith("doctor")) {
                List<Slot> slots = new ArrayList<>();
                StringBuilder someSlotsFoundResult = new StringBuilder();
                Boolean someSlotsFoundCheck = false;
                StringBuilder allSlotsFoundResult = new StringBuilder();
                Boolean allSlotsFoundCheck = false;
                for(Integer slotId : addSlotsRequest.getSlotIds()) {
                    Optional<Slot> slotById = slotRepo.findById(slotId);
                    if(slotById.isPresent()) {
                        Slot slot = slotById.get();
                        allSlotsFoundCheck = true;
                        allSlotsFoundResult.append("'"+slotId+"'"+" ");
                        slots.add(slot);
                        slot.setDoctor(doctor);
                        slotRepo.save(slot);
                    } else {
                        someSlotsFoundCheck = true;
                        someSlotsFoundResult.append("'"+slotId+"'"+" ");
                    }
                }
                if(allSlotsFoundCheck && someSlotsFoundCheck) {
                    doctor.getSlots().addAll(slots);
                    personRepo.save(doctor);
                    return "{ "+allSlotsFoundResult+"} : Slots added to doctor records ID : { "+doctor.getPersonId()+" }, Username : { "+doctor.getUsername()+" }. Some slots not found : { "+someSlotsFoundResult+"}.";
                } else if(allSlotsFoundCheck) {
                    doctor.getSlots().addAll(slots);
                    personRepo.save(doctor);
                    return "{ "+allSlotsFoundResult+"} : Slots added to doctor records ID : { "+doctor.getPersonId()+" }, Username : { "+doctor.getUsername()+" }.";
                } else {
                    return "No any slot found with ID : "+someSlotsFoundResult;
                }
            } else {
                throw new GeneralException("Username : { "+doctor.getUsername()+" } is a { "+doctor.getRole()+" }. Person must be doctor. Enter correct ID.");
            }
        } else {
            throw new GeneralException("Doctor not found with ID : "+doctorId);
        }
    }

    @Override
    public List<PersonResponse> getVaccinatedPatientsByDoctorId(Integer doctorId) throws GeneralException {
        Optional<Person> doctorById = personRepo.findById(doctorId);
        if(doctorById.isPresent()) {
            Person doctor = doctorById.get();
            if(doctor.getRole().toLowerCase().endsWith("doctor")) {
                if(!doctor.getDoctorVaccinationDetails().isEmpty() && doctor.getDoctorVaccinationDetails().size() != 0) {
                    List<VaccinationDetail> vaccinationDetails = doctor.getDoctorVaccinationDetails();
                    System.out.println("vaccinationDetails getVaccinatedPatientsByDoctorId : "+vaccinationDetails);
                    List<PersonResponse> personResponses = new ArrayList<>();
                    for(VaccinationDetail vaccinationDetail : vaccinationDetails) {
                        Person patient = vaccinationDetail.getPatient();
                        PersonResponse personResponse = modelMapper.map(patient, PersonResponse.class);
                        personResponses.add(personResponse);
                    }
                    return personResponses;
                } else {
                    throw new GeneralException("No any patients found in the vaccinationDetails records.");
                }
            } else {
                throw new GeneralException("Username : { "+doctor.getUsername()+" } is a { "+doctor.getRole()+" }. Person must be doctor. Enter correct ID.");
            }
        } else {
            throw new GeneralException("Doctor not found with ID : "+doctorId);
        }
    }

    @Override
    public List<PersonResponse> getPatientsFromAppointmentsByDoctorId(Integer doctorId) throws GeneralException {
        Optional<Person> doctorById = personRepo.findById(doctorId);
        if(doctorById.isPresent()) {
            Person doctor = doctorById.get();
            if(doctor.getRole().toLowerCase().endsWith("doctor")) {
                if(!doctor.getDoctorAppointmentDetails().isEmpty() && doctor.getDoctorAppointmentDetails().size() != 0) {
                    List<AppointmentDetail> appointmentDetails = doctor.getDoctorAppointmentDetails();
                    System.out.println("appointmentDetails getPatientsFromAppointmentsByDoctorId : "+appointmentDetails);
                    List<PersonResponse> personResponses = new ArrayList<>();
                    for(AppointmentDetail appointmentDetail : appointmentDetails) {
                        Person patient = appointmentDetail.getPatient();
                        PersonResponse personResponse = modelMapper.map(patient, PersonResponse.class);
                        personResponses.add(personResponse);
                    }
                    return personResponses;
                } else {
                    throw new GeneralException("No any patients found in the appointmentDetails records.");
                }
            } else {
                throw new GeneralException("Username : { "+doctor.getUsername()+" } is a { "+doctor.getRole()+" }. Person must be doctor. Enter correct ID.");
            }
        } else {
            throw new GeneralException("Doctor not found with ID : "+doctorId);
        }
    }

    @Override
    public List<AppointmentResponse> getAppointmentDetailsByDoctorId(Integer doctorId) throws GeneralException {
        Optional<Person> doctorById = personRepo.findById(doctorId);
        if(doctorById.isPresent()) {
            Person doctor = doctorById.get();
            if(doctor.getRole().toLowerCase().endsWith("doctor")) {
                if(!doctor.getDoctorAppointmentDetails().isEmpty() && doctor.getDoctorAppointmentDetails().size() != 0) {
                    List<AppointmentDetail> appointmentDetails = doctor.getDoctorAppointmentDetails();
                    List<AppointmentResponse> appointmentResponses = new ArrayList<>();
                    for (AppointmentDetail appointmentDetail : appointmentDetails) {
                        AppointmentResponse appointmentResponse = modelMapper.map(appointmentDetail, AppointmentResponse.class);
                        appointmentResponses.add(appointmentResponse);
                    }
                    return appointmentResponses;
                } else {
                    throw new GeneralException("No any appointment found for doctor ID : "+doctorId);
                }
            } else {
                throw new GeneralException("Username : { "+doctor.getUsername()+" } is a { "+doctor.getRole()+" }. Person must be doctor. Enter correct ID.");
            }
        } else {
            throw new GeneralException("Doctor not found with ID : "+doctorId);
        }
    }
//    @Override
//    public List<AppointmentResponse> getVaccinationDetailsByDoctorId(Integer doctorId) throws GeneralException {}

    @Override
    public List<PersonResponse> getAllDoctors() throws GeneralException {
        Optional<List<Person>> personByRole = personRepo.findByRole("ROLE_DOCTOR");
        if(personByRole.isPresent() && !personByRole.get().isEmpty()) {
            List<Person> doctors = personByRole.get();
            List<PersonResponse> personResponses = new ArrayList<>();
            for(Person doctor : doctors) {
                PersonResponse personResponse = modelMapper.map(doctor, PersonResponse.class);
                personResponses.add(personResponse);
            }
            return personResponses;
        } else {
            throw new GeneralException("No any doctor found in database.");
        }
    }

    @Override
    public List<SlotResponse> getAllSlots(Integer doctorId) throws GeneralException {
        Optional<Person> doctorById = personRepo.findById(doctorId);
        if(doctorById.isPresent()) {
            Person doctor = doctorById.get();
            if(doctor.getRole().toLowerCase().endsWith("doctor")) {
                List<Slot> slots = doctor.getSlots();
                if(!slots.isEmpty()) {
                    List<SlotResponse> slotResponses = new ArrayList<>();
                    for(Slot slot : slots) {
                        SlotResponse slotResponse = modelMapper.map(slot, SlotResponse.class);
                        slotResponse.setSlotTiming(slot.getStartTime()+" - "+slot.getEndTime());
                        slotResponses.add(slotResponse);
                    }
                    return slotResponses;
                } else {
                    throw new GeneralException("No any slots available for selected doctor ID : "+doctorId);
                }
            } else {
                throw new GeneralException("Username : { "+doctor.getUsername()+" } is a { "+doctor.getRole()+" }. Person must be doctor. Enter correct ID.");
            }
        } else {
            throw new GeneralException("Doctor not found with ID : "+doctorId);
        }
    }

    @Override
    public Person getOriginalDoctor(Integer doctorId) throws GeneralException {
        Optional<Person> personById = personRepo.findById(doctorId);
        if(personById.isPresent()) {
            Person person = personById.get();
            if(person.getRole().toLowerCase().endsWith("doctor")) {
                return person;
            } else {
                throw new GeneralException("Username : { "+person.getUsername()+" } is a { "+person.getRole()+" }. Person must be doctor. Enter correct ID.");
            }
        } else {
            throw new GeneralException("Doctor not found with ID : "+doctorId);
        }
    }
}
