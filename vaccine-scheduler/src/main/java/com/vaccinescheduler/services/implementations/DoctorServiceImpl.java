package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.dtos.request.AddSlots;
import com.vaccinescheduler.dtos.response.HospitalResponse;
import com.vaccinescheduler.dtos.response.PersonResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.Hospital;
import com.vaccinescheduler.models.Person;
import com.vaccinescheduler.models.Slot;
import com.vaccinescheduler.repositories.PersonRepo;
import com.vaccinescheduler.repositories.SlotRepo;
import com.vaccinescheduler.services.DoctorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
            if(doctor.getHospital() != null) {
                if(doctor.getRole().toLowerCase().endsWith("doctor")) {
                    Hospital hospital = doctor.getHospital();
                    HospitalResponse hospitalResponse = modelMapper.map(hospital, HospitalResponse.class);
                    return hospitalResponse;
                } else {
                    throw new GeneralException("Username : { "+doctor.getUsername()+" } is a { "+doctor.getRole()+" }. Person must be doctor. Enter correct ID.");
                }
            } else {
                throw new GeneralException("Doctor with ID { "+doctorId+" } is not associated with any hospital.");
            }
        } else {
            throw new GeneralException("Doctor not found with ID : "+doctorId);
        }
    }

    @Override
    public String addSlotsByDoctorId(AddSlots addSlots) throws GeneralException {
        Integer doctorId = addSlots.getDoctorId();
        Optional<Person> doctorById = personRepo.findById(doctorId);
        if(doctorById.isPresent()) {
            Person doctor = doctorById.get();
            List<Slot> slots = new ArrayList<>();
            StringBuilder someSlotsFoundResult = new StringBuilder();
            Boolean someSlotsFoundCheck = false;
            StringBuilder allSlotsFoundResult = new StringBuilder();
            Boolean allSlotsFoundCheck = false;
            for(Integer slotId : addSlots.getSlotIds()) {
                Optional<Slot> slotById = slotRepo.findById(slotId);
                if(slotById.isPresent()) {
                    allSlotsFoundCheck = true;
                    allSlotsFoundResult.append(slotId+" ");
                    slots.add(slotById.get());
                } else {
                    someSlotsFoundCheck = true;
                    someSlotsFoundResult.append(slotId+" ");
                }
            }
            if(allSlotsFoundCheck && someSlotsFoundCheck) {
                doctor.getSlots().addAll(slots);
                personRepo.save(doctor);
                return allSlotsFoundResult+": Slots added to records. Some slots not found : "+someSlotsFoundResult;
            } else if(allSlotsFoundCheck) {
                doctor.getSlots().addAll(slots);
                personRepo.save(doctor);
                return allSlotsFoundResult+": Slots added to records.";
            } else {
                return "No any slot found with ID : "+someSlotsFoundResult;
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
            if(!doctor.getPatients().isEmpty()) {
                List<PersonResponse> personResponses = new ArrayList<>();
                for (Person person : doctor.getPatients()) {
                    PersonResponse personResponse = modelMapper.map(person, PersonResponse.class);
                    personResponses.add(personResponse);
                }
                return personResponses;
            } else {
                throw new GeneralException("No any patients found in the records.");
            }
        } else {
            throw new GeneralException("Doctor not found with ID : "+doctorId);
        }
    }
}
