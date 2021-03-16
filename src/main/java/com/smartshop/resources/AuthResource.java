package com.smartshop.resources;


import com.smartshop.auth.CustomUserDetailsService;
import com.smartshop.dto.UserDto;
import com.smartshop.dtoMappers.UserMapper;
import com.smartshop.models.responses.Token;
import com.smartshop.models.User;
import com.smartshop.models.auth.AuthenticationRequest;
import com.smartshop.repositories.UserRepository;
import com.smartshop.utils.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
@Slf4j
public class AuthResource {

    private final UserRepository userRepository;

    private final CustomUserDetailsService userDetailsService;

    private final AuthenticationManager authenticationManager;

    private final UserMapper userMapper;

    private final JwtUtil jwtUtil;

    public AuthResource(UserRepository userRepository,
                        CustomUserDetailsService userDetailsService,
                        UserMapper userMapper,
                        AuthenticationManager authenticationManager,
                        JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.userMapper = userMapper;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }


    @PostMapping("/login")
    public ResponseEntity<?> loginAndCreateToken(
            @RequestBody AuthenticationRequest authenticationRequest) {

       try {
           this.authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
           );
       } catch(BadCredentialsException e) {
           log.info("Failed to login user");
           throw new BadCredentialsException("Email or password are invalid!");
       }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

        String jwt = this.jwtUtil.generateToken(userDetails);

        User registred = this.userRepository.findByEmail(this.jwtUtil.extractUsername(jwt)).get();

        Token token = new Token(jwt, this.jwtUtil.extractExpiration(jwt), userMapper.toDto(registred));

        return ResponseEntity.status(HttpStatus.OK).body(token);
    }


    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody UserDto user) {

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        User usr = userMapper.fromDto(user);

        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(this.userRepository.save(usr)));

    }

}

