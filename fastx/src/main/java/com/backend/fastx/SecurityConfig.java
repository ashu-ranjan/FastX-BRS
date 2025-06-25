package com.backend.fastx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration // <-- this ensures that class gets called during every api call
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Customer
                        .requestMatchers("/fastx/api/customer/add").permitAll()
                        // Operator
                        .requestMatchers("/fastx/api/bus-operator/add").permitAll()
                        // Executive
                        .requestMatchers("/fastx/api/executive/add").permitAll()
                        // User
                        .requestMatchers("/fastx/api/user/register").permitAll()
                        .requestMatchers("/fastx/api/user/token").permitAll()
                        .requestMatchers("/fastx/api/user/details").permitAll()
                        // Bus
                        .requestMatchers("/fastx/api/bus/add").hasAuthority("OPERATOR")
                        .requestMatchers("/fastx/api/bus/get-buses").hasAuthority("OPERATOR")
                        .requestMatchers("/fastx/api/bus/get-bus-type").hasAnyAuthority("OPERATOR", "EXECUTIVE")
                        .requestMatchers("/fastx/api/bus/add-amenities").hasAnyAuthority("OPERATOR", "EXECUTIVE")
                        .requestMatchers("/fastx/api/bus/get-amenities").permitAll()
                        // Seat
                        .requestMatchers("/fastx/api/add/seat/{busId}").hasAuthority("OPERATOR")
                        .requestMatchers("/fastx/api/get/seat").hasAuthority("CUSTOMER")
                        .requestMatchers("/fastx/api/get/seats/bus/{busId}").permitAll()
                        .requestMatchers("/fastx/api/update/seat/{seatId}").hasAnyAuthority("OPERATOR","EXECUTIVE")
                        // Bus Route
                        .requestMatchers("/fastx/api/bus-route/add").hasAnyAuthority("OPERATOR","EXECUTIVE")
                        .requestMatchers("/fastx/api/bus-route/get-all").hasAnyAuthority("OPERATOR","EXECUTIVE")
                        // Schedule
                        .requestMatchers("/fastx/api/schedules/create/bus/{busId}/route/{routeId}").hasAnyAuthority("OPERATOR","EXECUTIVE")
                        .requestMatchers("/fastx/api/schedules/all").permitAll()
                        .requestMatchers("/fastx/api/schedules/id/{id}").permitAll()
                        .requestMatchers("/fastx/api/schedules/search").hasAuthority("CUSTOMER")
                        // Booking
                        .requestMatchers("/fastx/api/book").hasAuthority("CUSTOMER")
                        .requestMatchers("/fastx/api/payment/make-payment").hasAuthority("CUSTOMER")
                        .requestMatchers("/fastx/api/booking/history").hasAuthority("CUSTOMER")
                        .requestMatchers("/fastx/api/bookings/{id}").hasAuthority("EXECUTIVE")
                        // Cancellation
                        .requestMatchers("/fastx/api/cancellation/request").hasAuthority("CUSTOMER")
                        .requestMatchers("/fastx/api/cancellation/approval").hasAuthority("EXECUTIVE")
                        .requestMatchers("/fastx/api/cancellation/get-all/pending").hasAuthority("EXECUTIVE")
                        .requestMatchers("/fastx/api/cancellation/history").hasAuthority("EXECUTIVE")
                        // Feedback
                        .requestMatchers("/fastx/api/feedback/submit").hasAuthority("CUSTOMER")
                        .requestMatchers("/fastx/api/feedback/get/by-operator").hasAuthority("OPERATOR")
                        .requestMatchers("/fastx/api/feedback/bus/{busId}").hasAuthority("CUSTOMER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){ // Bean saves this object in spring's context
        return new BCryptPasswordEncoder();
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }
}
