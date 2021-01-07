package com.company;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Qualifier("myUserDetailsService")
    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AuthSuccessHandler successHandler;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        //auth.userDetailsService(userDetailsService);
        auth.authenticationProvider(authenticationProvider());
//        auth
//                .inMemoryAuthentication()
//                .withUser("Lekarz").password(passwordEncoder().encode("Lekarz123")).roles("DOCTOR")
//                .and()
//                .withUser("Admin").password(passwordEncoder().encode("Admin123")).roles("ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .antMatchers("/admin").permitAll()//.hasRole("ADMIN")
                .antMatchers("/user").permitAll()//.hasAnyRole("ADMIN", "DOCTOR")
                .antMatchers("/patient").hasRole("PATIENT")
                .antMatchers("/clinic-worker").hasRole("CLINICWORKER")
                .antMatchers("/loginxd").permitAll()
                .antMatchers("/appointments/doctor-selection").permitAll()
                .antMatchers("/greeting").permitAll()
                .antMatchers("/appointments/term-selection").permitAll()
                .antMatchers("/").permitAll()
                .antMatchers("/clinic/create-patient/enter-data").permitAll()
                .antMatchers("/clinic/create-patient/has-pesel").permitAll()
                .antMatchers("/clinic/create-patient/already-exists").permitAll()
                .and().formLogin()
                .successHandler(successHandler);
    }

    @Bean
    DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        return NoOpPasswordEncoder.getInstance();
        //return new BCryptPasswordEncoder();
    }
}