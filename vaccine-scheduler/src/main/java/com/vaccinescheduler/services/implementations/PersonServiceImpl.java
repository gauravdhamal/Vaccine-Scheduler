package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.dtos.request.PersonRequest;
import com.vaccinescheduler.dtos.response.PersonResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.Person;
import com.vaccinescheduler.repositories.PersonRepo;
import com.vaccinescheduler.services.PersonService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {
    @Autowired
    private PersonRepo personRepo;
    @Autowired
    private ModelMapper modelMapper;
    @Override
    public PersonResponse createPerson(PersonRequest personRequest) throws GeneralException {
        String username = personRequest.getUsername();
        String aadhaarNumber = personRequest.getAadhaarNumber();
        Optional<Person> personByUsername = personRepo.findByUsername(username);
        if(personByUsername.isPresent()) throw new GeneralException("Person already present in database with username : { "+username+" } Choose another username.");
        Optional<Person> personByAadhaarNumber = personRepo.findByAadhaarNumber(aadhaarNumber);
        if(personByAadhaarNumber.isPresent()) throw new GeneralException("Person already present in database with aadhaarNumber : { "+aadhaarNumber+" } Enter your aadhar number..");
        Person person = modelMapper.map(personRequest, Person.class);
        String personRole = person.getRole();
        if(personRole.equalsIgnoreCase("admin")) person.setRole("ROLE_ADMIN");
        else if(personRole.equalsIgnoreCase("doctor")) person.setRole("ROLE_DOCTOR");
        else person.setRole("ROLE_PATIENT");
        person = personRepo.save(person);
        PersonResponse personResponse = modelMapper.map(person, PersonResponse.class);
        return personResponse;
    }

    @Override
    public PersonResponse updatePerson(Integer personId, PersonRequest personRequest) throws GeneralException {
        Optional<Person> personById = personRepo.findById(personId);
        if(personById.isPresent()) {
            Person oldPerson = personById.get();
            Person updatedPerson = modelMapper.map(personRequest, Person.class);
            if(oldPerson.getRole().toLowerCase().endsWith("admin")) {
                return this.updateAdmin(oldPerson, updatedPerson);
            } else if(oldPerson.getRole().toLowerCase().endsWith("doctor")) {
                return this.updateDoctor(oldPerson, updatedPerson);
            } else {
                return this.updatePatient(oldPerson, updatedPerson);
            }
        } else {
            throw new GeneralException("Person not found with ID : "+personId);
        }
    }
    private PersonResponse updateAdmin(Person oldPerson, Person updatedPerson) {
        if(updatedPerson.getInventory() != null) {

        }
        return null;
    }
    private PersonResponse updatePatient(Person oldPerson, Person updatedPerson) {
        return null;
    }
    private PersonResponse updateDoctor(Person oldPerson, Person updatedPerson) {
        return null;
    }
    @Override
    public PersonResponse getPerson(Integer personId) throws GeneralException {
        Optional<Person> personById = personRepo.findById(personId);
        if(personById.isPresent()) {
            Person person = personById.get();
            PersonResponse personResponse = modelMapper.map(person, PersonResponse.class);
            return personResponse;
        } else {
            throw new GeneralException("Person not found with ID : "+personId);
        }
    }

    @Override
    public PersonResponse getPersonByUsername(String username) throws GeneralException {
        Optional<Person> personById = personRepo.findByUsername(username);
        if(personById.isPresent()) {
            Person person = personById.get();
            PersonResponse personResponse = modelMapper.map(person, PersonResponse.class);
            return personResponse;
        } else {
            throw new GeneralException("Person not found with username : "+username);
        }
    }

    @Override
    public PersonResponse getPersonAadhaarNumber(String aadhaarNumber) throws GeneralException {
        Optional<Person> personById = personRepo.findByAadhaarNumber(aadhaarNumber);
        if(personById.isPresent()) {
            Person person = personById.get();
            PersonResponse personResponse = modelMapper.map(person, PersonResponse.class);
            return personResponse;
        } else {
            throw new GeneralException("Person not found with aadhaarNumber : "+aadhaarNumber);
        }
    }

    @Override
    public Boolean deletePerson(Integer personId) throws GeneralException {
        Optional<Person> personById = personRepo.findById(personId);
        if(personById.isPresent()) {
            Person person = personById.get();
            personRepo.delete(person);
            return true;
        } else {
            throw new GeneralException("Person not found with ID : "+personId);
        }
    }

    @Override
    public List<PersonResponse> getAllPerson() throws GeneralException {
        List<Person> personList = personRepo.findAll();
        List<PersonResponse> personResponsesList = new ArrayList<>();
        if(!personList.isEmpty()) {
            for(Person person : personList) {
                PersonResponse personResponse = modelMapper.map(person, PersonResponse.class);
                personResponsesList.add(personResponse);
            }
            return personResponsesList;
        } else {
            throw new GeneralException("No any person found in database.");
        }
    }
}
