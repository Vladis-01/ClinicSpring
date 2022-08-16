package com.example.veterinaryclinic.spring.config;

import com.example.veterinaryclinic.spring.services.DoctorService;
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
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(2)
public class WebSecurityConfigDoctor extends WebSecurityConfigurerAdapter {
    @Autowired
    private DoctorService doctorService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/doctor/**")
                .authorizeRequests()
                .antMatchers("/doctor/registration", "/doctor/login").anonymous().anyRequest().authenticated()
                .and().exceptionHandling().accessDeniedPage("/checkUser")
                .and()
                .formLogin()
                .loginPage("/doctor/login")
                .defaultSuccessUrl("/doctor")
                .and()
                .logout()
                .logoutUrl("/doctor/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(doctorService)
                .passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}