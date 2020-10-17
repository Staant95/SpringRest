package com.smartshop.resources;


import com.smartshop.auth.CustomUserDetailsService;
import com.smartshop.models.Token;
import com.smartshop.models.User;
import com.smartshop.models.auth.AuthenticationRequest;
import com.smartshop.repositories.TokenRespository;
import com.smartshop.repositories.UserRepository;
import com.smartshop.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRespository tokenRespository;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Token> createAuthenticationToken(@Valid @RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getEmail());

        User optionalUser = userRepository.findByEmail(userDetails.getUsername()).get();

        User user = optionalUser;

        // Check if user has already a token and it's valid
        if(user.getToken() != null) {

            if(jwtUtil.validateToken(user.getToken().getToken(), userDetails)) {

                return ResponseEntity.ok(user.getToken());
            }
            // token is invalid, delete from database and continue
            user.removeToken(user.getToken());
        }

        final String jwt = jwtUtil.generateToken(userDetails);

        Token token = new Token(jwt, jwtUtil.extractExpiration(jwt), user);
        user.addToken(token);
        User saved = this.userRepository.save(user);

        return ResponseEntity.ok(saved.getToken());
    }


    @PostMapping("/register")
    public User register(@Valid @RequestBody User user) {
        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        return this.userRepository.save(user);
    }

}

