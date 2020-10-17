package com.smartshop.resources;

import com.smartshop.auth.CustomUserDetailsService;
import com.smartshop.models.Token;
import com.smartshop.models.User;
import com.smartshop.models.auth.AuthenticationRequest;
import com.smartshop.repositories.TokenRespository;
import com.smartshop.repositories.UserRepository;
import com.smartshop.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/test")
public class Test {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRespository tokenRespository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping()
    public User test(@RequestBody User user) {


        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        User saved = this.userRepository.save(user);
        return saved;
    }

    @PostMapping("login")
    public UserDetails login(@RequestBody AuthenticationRequest user) {

        return userDetailsService.loadUserByUsername(user.getEmail());

    }
}
