package com.vaccinescheduler.services.implementations;

import com.vaccinescheduler.models.MyUserDetails;
import com.vaccinescheduler.models.Person;
import com.vaccinescheduler.repositories.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {
    @Autowired
    private PersonRepo personRepo;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<Person> personByUsername = personRepo.findByUsername(username);
        personByUsername.orElseThrow(() -> new UsernameNotFoundException("Person not found with username : "+username));
        return new MyUserDetails(personByUsername.get());
    }
}
