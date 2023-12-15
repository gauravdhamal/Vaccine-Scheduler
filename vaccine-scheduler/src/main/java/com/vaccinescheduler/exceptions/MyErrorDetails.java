package com.vaccinescheduler.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MyErrorDetails {
    private LocalDateTime timeStamp;
    private String message;
    private String description;
}
