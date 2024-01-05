package com.vaccinescheduler.controllers;

import com.vaccinescheduler.dtos.response.PersonResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.models.Person;
import com.vaccinescheduler.repositories.PersonRepo;
import com.vaccinescheduler.services.PersonService;
import com.vaccinescheduler.services.implementations.JavaEmailService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@CrossOrigin(origins = "*")
//@RequestMapping("/email")
public class EmailController {
    @Autowired
    private JavaEmailService javaEmailService;
    @Autowired
    private PersonService personService;
    @Autowired
    private PersonRepo personRepo;
    @Autowired
    private ModelMapper modelMapper;
    @GetMapping("/send")
    public ResponseEntity<String> sendEmail() throws GeneralException {
        PersonResponse personByUsername = personService.getPersonByUsername("priyashinde");
        Person person = modelMapper.map(personByUsername, Person.class);
        person.setFirstName("Dr. Priyanka");
        Person savedPerson = personRepo.save(person);
        System.out.println("savedPerson : "+savedPerson);
        personRepo.save(person);
        personRepo.save(person);
        personRepo.save(person);
        personRepo.save(person);
        personRepo.save(person);
        javaEmailService.sendEmail("gauravdhamal11@gmail.com", "My first email", "Hello you there?");
        return new ResponseEntity<>("Email sent.", HttpStatus.OK);
    }
}
