package com.smartshop.auth;

import com.smartshop.models.User;
import com.smartshop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByEmail(email);

        if(user.isEmpty()) throw new UsernameNotFoundException("No user found with this email: " + email);

        User u1 = user.get();

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(u1.getEmail())
                .password("{bcrypt}" + u1.getPassword())
                .roles(u1.getRole())
                .build();

        return userDetails;
    }
}
