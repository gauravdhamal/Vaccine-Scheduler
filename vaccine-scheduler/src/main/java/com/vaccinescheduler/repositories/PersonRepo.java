package com.vaccinescheduler.repositories;

import com.vaccinescheduler.models.Person;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonRepo extends JpaRepository<Person, Integer> {
    Optional<Person> findByUsername(String username);
    Optional<Person> findByAadhaarNumber(String aadhaarNumber);
}
