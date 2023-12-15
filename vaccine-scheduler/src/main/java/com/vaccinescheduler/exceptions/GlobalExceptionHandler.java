package com.vaccinescheduler.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(GeneralException.class)
    public ResponseEntity<MyErrorDetails> userExceptionHandler(GeneralException generalException, WebRequest webRequest) {
        System.out.println("Inside userExceptionHandler.");
        MyErrorDetails myErrorDetails = new MyErrorDetails();
        myErrorDetails.setTimeStamp(LocalDateTime.now());
        myErrorDetails.setMessage(generalException.getMessage());
        myErrorDetails.setDescription(webRequest.getDescription(false));
        return new ResponseEntity<MyErrorDetails>(myErrorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MyErrorDetails> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException methodArgumentNotValidException) {
        System.out.println("Inside methodArgumentNotValidExceptionHandler.");
        MyErrorDetails myErrorDetails = new MyErrorDetails();
        myErrorDetails.setTimeStamp(LocalDateTime.now());
        myErrorDetails.setMessage("Validation failed.");
        myErrorDetails.setDescription(methodArgumentNotValidException.getBindingResult().getFieldError().getDefaultMessage());
        return new ResponseEntity<MyErrorDetails>(myErrorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<MyErrorDetails> noHandlerFoundExceptionHandler(NoHandlerFoundException noHandlerFoundException, WebRequest webRequest) {
        System.out.println("Inside noNoHandlerFoundExceptionHandler.");
        MyErrorDetails myErrorDetails = new MyErrorDetails();
        myErrorDetails.setTimeStamp(LocalDateTime.now());
        myErrorDetails.setMessage(noHandlerFoundException.getMessage());
        myErrorDetails.setDescription(webRequest.getDescription(false));
        return new ResponseEntity<>(myErrorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<MyErrorDetails> illegalArgumentExceptionHandler(IllegalArgumentException illegalArgumentException, WebRequest webRequest) {
        System.out.println("Inside illegalArgumentExceptionHandler");
        MyErrorDetails myErrorDetails = new MyErrorDetails();
        myErrorDetails.setTimeStamp(LocalDateTime.now());
        myErrorDetails.setMessage(illegalArgumentException.getMessage());
        myErrorDetails.setDescription(webRequest.getDescription(false));
        return new ResponseEntity<>(myErrorDetails, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MyErrorDetails> exceptionHandler(Exception exception, WebRequest webRequest) {
        System.out.println("Inside exceptionHandler");
        MyErrorDetails myErrorDetails = new MyErrorDetails();
        myErrorDetails.setTimeStamp(LocalDateTime.now());
        myErrorDetails.setMessage(exception.getMessage());
        myErrorDetails.setDescription(webRequest.getDescription(false));
        return new ResponseEntity<>(myErrorDetails, HttpStatus.BAD_REQUEST);
    }
}
