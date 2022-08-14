package com.example.veterinaryclinic.spring.config;

import com.example.veterinaryclinic.spring.services.DoctorService;
import com.example.veterinaryclinic.spring.services.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;

@Configuration
@EnableWebSecurity
@Order(1)
public class WebSecurityConfigPatient extends WebSecurityConfigurerAdapter {
    @Autowired
    private PatientService patientService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/patient/**")
                .authorizeRequests()
                .antMatchers("/patient/registration", "/patient/login").anonymous().anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/patient/login")
                .defaultSuccessUrl("/patient")
                .and()
                .logout()
                .logoutUrl("/patient/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(patientService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}