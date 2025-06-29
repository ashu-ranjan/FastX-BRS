package com.backend.fastx.service;

import com.backend.fastx.model.User;
import com.backend.fastx.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service // This annotation tells Spring to create an object of this class and use it wherever required
public class CustomUserDetailService implements UserDetailsService { // This class implements UserDetailsService to provide user details for authentication

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // fetch user by given username or email
        User user = userRepository.findByUsername(username)
                .orElseThrow(()-> new UsernameNotFoundException("User not found"));

        // Convert your Role into Authority as spring works with authority
        SimpleGrantedAuthority sga = new SimpleGrantedAuthority(user.getRole().name());

        // Add this SimpleGrantedAuthority Object (sga) into the List now
        List<GrantedAuthority> list = List.of(sga);

        // Convert our User to spring's User that is UserDetails
        org.springframework.security.core.userdetails.User springUser =
                new org.springframework.security.core.userdetails.User
                        (user.getUsername(),
                                user.getPassword(),
                                list);
        return springUser;
    }

    /**
     * This method is used to load user details by username.
     * It fetches the user from the database and converts it into Spring's UserDetails object.
     * 
     * this class is used by spring security to load the user details during authentication.
     * It is automatically called by spring security when a user tries to authenticate.
     * 
     */
}
