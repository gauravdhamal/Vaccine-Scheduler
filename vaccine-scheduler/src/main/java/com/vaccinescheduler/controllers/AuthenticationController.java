package com.vaccinescheduler.controllers;

import com.vaccinescheduler.dtos.request.AuthenticationRequest;
import com.vaccinescheduler.dtos.response.AuthenticationResponse;
import com.vaccinescheduler.exceptions.GeneralException;
import com.vaccinescheduler.jwt.util.JwtUtil;
import com.vaccinescheduler.services.implementations.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/authentication")
public class AuthenticationController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private JwtUtil jwtUtil;
    @GetMapping("/home")
    public String hello() {
        return "Hello World...!!!";
    }
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@Valid @RequestBody AuthenticationRequest authenticationRequest) throws GeneralException {
        try {
            authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getUsername(), authenticationRequest.getPassword())
                    );
        } catch (BadCredentialsException e) {
            throw new GeneralException("Invalid username or password.");
        }
        final UserDetails userDetails = myUserDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        token = "Bearer " + token;
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        authenticationResponse.setUsername(userDetails.getUsername());
        authenticationResponse.setRole(userDetails.getAuthorities().toString());
        authenticationResponse.setJwt(token);
        return ResponseEntity.ok(authenticationResponse);
    }
}
