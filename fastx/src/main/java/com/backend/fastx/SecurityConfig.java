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
    private JwtFilter jwtFilter; // This is the filter that will intercept every request and check for JWT token

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        // Customer
                        .requestMatchers("/fastx/api/customer/add").permitAll()
                        .requestMatchers("/fastx/api/customer/update/profile").permitAll()
                        .requestMatchers("/fastx/api/customer/get/profile").permitAll()
                        .requestMatchers("/fastx/api/customer/upload/image").permitAll()
                        // Operator
                        .requestMatchers("/fastx/api/bus-operator/add").permitAll()
                        .requestMatchers("null/fastx/api/bus-operator/update/profile").permitAll()
                        .requestMatchers("/fastx/api/bus-operator/upload/image").permitAll()
                        .requestMatchers("/fastx/api/bus-operator/get/profile").permitAll()
                        // Executive
                        .requestMatchers("/fastx/api/executive/add").permitAll()
                        .requestMatchers("/fastx/api/executive/update/profile").permitAll()
                        .requestMatchers("/fastx/api/executive/upload/image").permitAll()
                        .requestMatchers("/fastx/api/executive/get/profile").permitAll()

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
                        .requestMatchers("/fastx/api/bus/update/{busId}").permitAll()
                        // Driver
                        .requestMatchers("/fastx/api/driver/add/{busId}").hasAuthority("OPERATOR")
                        .requestMatchers("/fastx/api/driver/bus/{busId}").authenticated()
                        .requestMatchers("/fastx/api/driver/update/{driverId}/bus/{busId}").hasAuthority("OPERATOR")
                        .requestMatchers("/fastx/api/driver/delete/{driverId}/bus/{busId}").hasAuthority("OPERATOR")
                        .requestMatchers("/fastx/api/driver/all").hasAuthority("OPERATOR")
                        .requestMatchers("/fastx/api/driver/{driverId}").hasAuthority("OPERATOR")  
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
                        .requestMatchers("/fastx/api/schedules/search").permitAll()
                        .requestMatchers("/fastx/api/schedules/bus/{busId}").hasAuthority("OPERATOR")
                        // Booking
                        .requestMatchers("/fastx/api/book").hasAuthority("CUSTOMER")
                        .requestMatchers("/fastx/api/payment/make-payment").hasAuthority("CUSTOMER")
                        .requestMatchers("/fastx/api/booking/history").hasAuthority("CUSTOMER")
                        .requestMatchers("/fastx/api/bookings/{id}").hasAuthority("EXECUTIVE")
                        .requestMatchers("/fastx/api/booking-details/by-bookingId/{bookingId}").hasAuthority("CUSTOMER")
                        // Cancellation
                        .requestMatchers("/fastx/api/cancellation/request/{bookingDetailId}").hasAuthority("CUSTOMER")
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
                .httpBasic(Customizer.withDefaults()); // Enable basic authentication
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){ // Bean saves this object in spring's context
        return new BCryptPasswordEncoder();
    }

    // This bean is used to create an AuthenticationManager that is used by Spring Security to authenticate users
    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration auth) throws Exception {
        return auth.getAuthenticationManager();
    }
}
