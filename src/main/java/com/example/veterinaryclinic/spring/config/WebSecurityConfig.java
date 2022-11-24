package com.example.veterinaryclinic.spring.config;

import com.example.veterinaryclinic.spring.services.DoctorService;
import com.example.veterinaryclinic.spring.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final DoctorService doctorService;
    private final PatientService patientService;

    public WebSecurityConfig(DoctorService doctorService, PatientService patientService) {
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/doctor/**").hasAnyAuthority("ADMIN", "DOCTOR")
                .antMatchers("/patient/**").hasAuthority("USER")
                .antMatchers("/static/**").permitAll()
                .antMatchers("/registration", "/login", "/")
                .anonymous().anyRequest().authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/checkUser")
                .and()
                .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/checkUser")
                .and()
                .logout()
                .logoutUrl("/logout");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(doctorService).passwordEncoder(new BCryptPasswordEncoder()).and().userDetailsService(patientService)
                .passwordEncoder(new BCryptPasswordEncoder());
    }
}