package com.vaccinescheduler.controllers;

import com.vaccinescheduler.dtos.request.PersonRequest;
import com.vaccinescheduler.dtos.response.PersonResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import javax.validation.Valid;
@RestController
@RequestMapping("/person")
public class PersonController {
    @Autowired
    private PersonService personService;
    @PostMapping("/create")
    public ResponseEntity<PersonResponse> createPerson(@Valid @RequestBody PersonRequest personRequest) throws GeneralException {
        PersonResponse personResponse = personService.createPerson(personRequest);
        return new ResponseEntity<>(personResponse, HttpStatus.CREATED);
    }
    @PutMapping("/update/{personId}")
    public ResponseEntity<PersonResponse> updatePerson(@PathVariable(value = "personId") Integer personId,@Valid @RequestBody PersonRequest personRequest) throws GeneralException {
        PersonResponse personResponse = personService.updatePerson(personId, personRequest);
        return new ResponseEntity<>(personResponse, HttpStatus.OK);
    }
    @GetMapping("/get/{personId}")
    public ResponseEntity<PersonResponse> getPerson(@PathVariable(value = "personId") Integer personId) throws GeneralException {
        PersonResponse personResponse = personService.getPerson(personId);
        return new ResponseEntity<>(personResponse, HttpStatus.OK);
    }
    @GetMapping("/byUsername/{username}")
    public ResponseEntity<PersonResponse> getPersonByUsername(@PathVariable(value = "username") String username) throws GeneralException {
        PersonResponse personByUsername = personService.getPersonByUsername(username);
        return new ResponseEntity<>(personByUsername, HttpStatus.OK);
    }
    @GetMapping("/byAadhaarNumber/{aadhaarNumber}")
    public ResponseEntity<PersonResponse> getPersonAadhaarNumber(@PathVariable(value = "aadhaarNumber") String aadhaarNumber) throws GeneralException {
        PersonResponse personAadhaarNumber = personService.getPersonAadhaarNumber(aadhaarNumber);
        return new ResponseEntity<>(personAadhaarNumber, HttpStatus.OK);
    }
    @DeleteMapping("/delete/{personId}")
    public ResponseEntity<Boolean> deletePerson(@PathVariable(value = "personId") Integer personId) throws GeneralException {
        Boolean deleted = personService.deletePerson(personId);
        return new ResponseEntity<>(deleted, HttpStatus.NO_CONTENT);
    }
    @GetMapping("/all")
    public ResponseEntity<List<PersonResponse>> getAllPerson() throws GeneralException {
        List<PersonResponse> allPerson = personService.getAllPerson();
        return new ResponseEntity<>(allPerson, HttpStatus.OK);
    }
}
