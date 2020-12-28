package com.smartshop.resources;


import com.smartshop.auth.CustomUserDetailsService;
import com.smartshop.dto.UserDto;
import com.smartshop.dtoMappers.UserMapper;
import com.smartshop.models.Token;
import com.smartshop.models.User;
import com.smartshop.models.auth.AuthenticationRequest;
import com.smartshop.repositories.UserRepository;
import com.smartshop.services.LoginService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthResource {

    private final LoginService loginService;

    private final UserRepository userRepository;

    private final CustomUserDetailsService userDetailsService;

    private final UserMapper userMapper;

    public AuthResource(LoginService loginService, UserRepository userRepository, CustomUserDetailsService userDetailsService, UserMapper userMapper) {
        this.loginService = loginService;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.userMapper = userMapper;
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginAndCreateToken(
            @Valid @RequestBody AuthenticationRequest authenticationRequest) {

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getEmail());

        log.info("HIT");

        User user = userRepository.findByEmail(userDetails.getUsername()).get();
        log.info("HIT");
        Token token = loginService.createToken(user, userDetails);

        user.addToken(token);

        User saved = this.userRepository.save(user);

        return ResponseEntity.ok(saved.getToken());
    }


    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto user) {

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        User usr = userMapper.fromDto(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(userMapper.toDto(this.userRepository.save(usr)));

    }


}

