package com.smartshop.resources;


import com.smartshop.auth.CustomUserDetailsService;
import com.smartshop.dto.UserDto;
import com.smartshop.dtoMappers.UserMapper;
import com.smartshop.models.Token;
import com.smartshop.models.User;
import com.smartshop.models.auth.AuthenticationRequest;
import com.smartshop.repositories.UserRepository;
import com.smartshop.services.LoginService;
import com.smartshop.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthResource {

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UserMapper userMapper;


    @PostMapping("/login")
    public ResponseEntity<?> loginAndCreateToken(
            @Valid @RequestBody AuthenticationRequest authenticationRequest) {

        if(!this.loginService.authenticateUser(authenticationRequest))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Email or password are incorrect"));

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getEmail());

        User user = userRepository.findByEmail(userDetails.getUsername()).get();

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

