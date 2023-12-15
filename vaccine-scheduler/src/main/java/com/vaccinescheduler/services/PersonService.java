package com.vaccinescheduler.services;

import com.vaccinescheduler.dtos.request.PersonRequest;
import com.vaccinescheduler.dtos.response.PersonResponse;
import com.vaccinescheduler.exceptions.GeneralException;

import java.util.List;

public interface PersonService {
    PersonResponse createPerson(PersonRequest personRequest) throws GeneralException;
    PersonResponse updatePerson(Integer personId, PersonRequest personRequest) throws GeneralException;
    PersonResponse getPerson(Integer personId) throws GeneralException;
    PersonResponse getPersonByUsername(String username) throws GeneralException;
    PersonResponse getPersonAadhaarNumber(String aadhaarNumber) throws GeneralException;
    Boolean deletePerson(Integer personId) throws GeneralException;
    List<PersonResponse> getAllPerson() throws GeneralException;
}
