package com.smartshop.resources;


import com.smartshop.auth.CustomUserDetailsService;
import com.smartshop.dto.UserDto;
import com.smartshop.dtoMappers.UserMapper;
import com.smartshop.exceptionHandlers.DuplicateEmailException;
import com.smartshop.exceptionHandlers.InvalidLoginCredentialsException;
import com.smartshop.models.auth.RegistrationForm;
import com.smartshop.models.responses.Token;
import com.smartshop.models.User;
import com.smartshop.models.auth.LoginForm;
import com.smartshop.repositories.UserRepository;
import com.smartshop.utils.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthResource {
    private final Logger log = LoggerFactory.getLogger(AuthResource.class);

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
    public ResponseEntity<Token> loginAndCreateToken(
            @RequestBody LoginForm authenticationRequest) {

       try {
           this.authenticationManager.authenticate(
                   new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(), authenticationRequest.getPassword())
           );
       } catch(BadCredentialsException e) {
           log.info("Failed to login user");
           throw new InvalidLoginCredentialsException();
       }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());

        String jwt = this.jwtUtil.generateToken(userDetails);

        User registred = this.userRepository.findByEmail(this.jwtUtil.extractUsername(jwt))
                            .orElseThrow(EntityNotFoundException::new);

        Token token = new Token(jwt, this.jwtUtil.extractExpiration(jwt), userMapper.toDto(registred));

        return ResponseEntity.status(HttpStatus.OK).body(token);
    }


    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@Valid @RequestBody RegistrationForm user) {

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));

        User us1 = new User(user.getName(),user.getLastname(),user.getEmail(),user.getPassword());

        User usr;

        try {
            usr = this.userRepository.save(us1);
        } catch(RuntimeException ex) {
            throw new DuplicateEmailException();
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(userMapper.toDto(usr));
    }

}

