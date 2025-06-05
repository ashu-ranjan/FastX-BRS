package com.backend.fastx;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                        // Customer
                        .requestMatchers("/fastx/api/customer/add").permitAll()
                        // Operator
                        .requestMatchers("/fastx/api/bus-operator/add").permitAll()
                        // Executive
                        .requestMatchers("/fastx/api/executive/add").permitAll()
                        // User
                        .requestMatchers("/fastx/api/user/register").permitAll()
                        .requestMatchers("/fastx/api/user/token").permitAll()
                        // Bus
                        .requestMatchers("/fastx/api/bus/add").hasAuthority("OPERATOR")
                        // Seat
                        .requestMatchers("/fastx/add/seat/{busId}").hasAuthority("OPERATOR")
                        // Bus Route
                        .requestMatchers("/fastx/api/bus-route/add").permitAll()
                        // Schedule
                        .requestMatchers("/fastx/api/schedules/create/bus/{busId}/route/{routeId}").hasAnyAuthority("OPERATOR","EXECUTIVE")
                        .requestMatchers("/fastx/api/schedules/all").permitAll()
                        .requestMatchers("/fastx/api/schedules/id/{id}").permitAll()
                        .requestMatchers("/fastx/api/schedules/search").hasAuthority("CUSTOMER")
                        // Booking
                        .requestMatchers("/fastx/api/book").permitAll()
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
