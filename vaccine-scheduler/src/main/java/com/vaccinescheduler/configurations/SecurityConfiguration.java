package com.vaccinescheduler.configurations;

import com.vaccinescheduler.jwt.filter.JwtAccessDeniedHandler;
import com.vaccinescheduler.jwt.filter.JwtEntryPointFilter;
import com.vaccinescheduler.jwt.filter.JwtRequestFilter;
import com.vaccinescheduler.services.implementations.MyUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
public class SecurityConfiguration {
    @Autowired
    private JwtEntryPointFilter jwtEntryPointFilter;
    @Autowired
    private JwtAccessDeniedHandler jwtAccessDeniedHandler;
    @Autowired
    private MyUserDetailsService myUserDetailsService;
    @Autowired
    private JwtRequestFilter jwtRequestFilter;
    public static final String[] PUBLIC_URIS = {
            "/v3/api-docs",
            "/v2/api-docs",
            "/swagger-resources/**",
            "/swagger-ui/**",
            "/webjars/**",
            "/authentication/authenticate",
            "/authentication/home",
            "/slot/getSlotsByVaccineName/{vaccineName}",
            "/appointment/get/{appointmentDetailId}",
            "/appointment/book/{slotId}/{hospitalId}",
            "/payment/pay/{appointmentDetailId}",
            "/person/create",
            "/email/send",
            "/appointment/reschedule/{newSlotId}/{appointmentId}",
    };
    public static final String[] PATIENT_URIS = {
            "/patient/appointments/{patientId}",
            "/patient/vaccinations/{patientId}",
    };
    public static final String[] DOCTOR_URIS = {
            "/doctor/getHospitalByDoctorId/{doctorId}",
            "/doctor/getVaccinatedPatientsByDoctorId/{doctorId}",
            "/doctor/getPatientsFromAppointmentsByDoctorId/{doctorId}",
            "/doctor/getAppointmentDetailsByDoctorId/{doctorId}",
            "/doctor/get/{doctorId}/allSlots",
    };
    public static final String[] ADMIN_URIS = {
            "/doctor/all",
            "/doctor/addSlots",
            "/hospital/create",
            "/hospital/get/{hospitalId}",
            "/hospital/update/{hospitalId}",
            "/hospital/delete/{hospitalId}",
            "/hospital/addInventoryToHospital/{hospitalId}/{inventoryId}",
            "/hospital/allPayments/{hospitalId}",
            "/hospital/getAllAppointments/{hospitalId}",
            "/hospital/addDoctorsToHospital",
            "/hospital/all",
            "/inventory/create",
            "/inventory/get/{inventoryId}",
            "/inventory/update/{inventoryId}",
            "/inventory/delete/{inventoryId}",
            "/inventory/all",
            "/inventory/allVaccinesByInventoryId/{inventoryId}",
            "/inventory/assignManagerToInventory/{inventoryId}/{managerId}",
            "/inventory/addVaccinesToInventory",
            "/patient/all",
            "/payment/update/{paymentDetailId}",
            "/payment/get/{paymentDetailId}",
            "/payment/delete/{paymentDetailId}",
            "/person/update/{personId}",
            "/person/get/{personId}",
            "/person/byUsername/{username}",
            "/person/byAadhaarNumber/{aadhaarNumber}",
            "/person/delete/{personId}",
            "/person/all",
            "/slot/create",
            "/slot/get/{slotId}",
            "/slot/update/{slotId}",
            "/slot/delete/{slotId}",
            "/slot/all",
            "/vaccine/create",
            "/vaccine/get/{vaccineId}",
            "/vaccine/update/{vaccineId}",
            "/vaccine/delete/{vaccineId}",
            "/vaccine/all",
            "/vaccine/adult",
            "/vaccine/child",
            "/vaccine/getByName/{vaccineName}",
            "/admin/updateInventoryManager/{adminId}/{inventoryId}",
            "/admin/all",
            "/vaccination/updateRecord",
            "/vaccination/all",
            "/vaccination/details/{date}/{slot}",
    };
    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(this.passwordEncoder());
        return daoAuthenticationProvider;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(jwtEntryPointFilter)
                .and()
                .authorizeRequests()
                .antMatchers(PUBLIC_URIS).permitAll()
                .antMatchers(PATIENT_URIS).hasAnyRole("PATIENT", "ADMIN")
                .antMatchers(DOCTOR_URIS).hasAnyRole("DOCTOR", "ADMIN")
                .antMatchers(ADMIN_URIS).hasRole("ADMIN")
                .anyRequest().authenticated()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http
                .authenticationProvider(this.daoAuthenticationProvider());

        http
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                .authenticationEntryPoint(jwtEntryPointFilter)
                .accessDeniedHandler(jwtAccessDeniedHandler);

        DefaultSecurityFilterChain defaultSecurityFilterChain = http.build();

        return defaultSecurityFilterChain;
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}
