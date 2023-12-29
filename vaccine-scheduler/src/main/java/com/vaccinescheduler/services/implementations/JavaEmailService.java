package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.exceptions.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class JavaEmailService {
    @Autowired
    private JavaMailSender javaMailSender;
    public void sendEmail(String to, String subject, String body) throws GeneralException {
        if(to == null) throw new GeneralException("Email 'to' must not be null.");
        if(subject == null) throw new GeneralException("Email 'subject' must not be null.");
        if(body == null) throw new GeneralException("Email message 'body' must not be null.");
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setFrom("no-reply@noduco.com");
        simpleMailMessage.setTo(to);
        simpleMailMessage.setText(body);
        simpleMailMessage.setSubject(subject);
        javaMailSender.send(simpleMailMessage);
        System.out.println("Email sent...");
    }
}
