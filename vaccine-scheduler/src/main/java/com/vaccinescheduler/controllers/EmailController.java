package com.vaccinescheduler.controllers;

import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.services.implementations.JavaEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {
    @Autowired
    private JavaEmailService javaEmailService;
    @GetMapping("/send")
    public ResponseEntity<String> sendEmail() throws GeneralException {
        javaEmailService.sendEmail("gauravdhamal11@gmail.com", "My first email", "Hello you there?");
        return new ResponseEntity<>("Email sent.", HttpStatus.OK);
    }
}
