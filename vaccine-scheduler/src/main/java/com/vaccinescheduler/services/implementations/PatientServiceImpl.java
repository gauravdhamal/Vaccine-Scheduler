package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.dtos.response.AppointmentResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.AppointmentDetail;
import com.vaccinescheduler.models.Person;
import com.vaccinescheduler.repositories.PersonRepo;
import com.vaccinescheduler.services.PatientService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {
    @Autowired
    private PersonRepo personRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public List<AppointmentResponse> getAppointments(Integer patientId) throws GeneralException {
        Optional<Person> personById = personRepo.findById(patientId);
        if(personById.isPresent()) {
            Person person = personById.get();
            if(!person.getAppointmentDetails().isEmpty()) {
                List<AppointmentResponse> appointmentResponses = new ArrayList<>();
                List<AppointmentDetail> appointmentDetails = person.getAppointmentDetails();
                for(AppointmentDetail appointmentDetail : appointmentDetails) {
                    AppointmentResponse appointmentResponse = modelMapper.map(appointmentDetail, AppointmentResponse.class);
                    appointmentResponses.add(appointmentResponse);
                }
                return appointmentResponses;
            } else {
                throw new GeneralException("There are no any appointments scheduled or which was completed in past.");
            }
        } else {
            throw new GeneralException("Patient not found in database.");
        }
    }
}
