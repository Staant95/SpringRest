package com.smartshop.resources;


import com.smartshop.auth.JwtUtil;
import com.smartshop.models.AuthRequest;
import com.smartshop.models.Token;
import com.smartshop.models.User;
import com.smartshop.repositories.TokenRespository;
import com.smartshop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/")
public class AuthResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenRespository tokenRespository;


    @PostMapping(path = "/login")
    public Token login(@RequestBody AuthRequest request) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
        } catch(BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect login data");
        }

        Optional<User> optionalUser = this.userRepository.findByEmail(request.getEmail());

        User user = optionalUser.isPresent() ? optionalUser.get() : null;

        if(user.getToken() != null) return user.getToken();

        return storeUserToken(user);
    }

    @PostMapping(path = "/register")
    public User register(@RequestBody User user) {

        user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
        User savedUser = this.userRepository.save(user);

        return savedUser;
    }


    private Token storeUserToken(User user) {

        String userToken = jwtUtil.generateToken(user.getEmail());

        Token token = new Token();
        token.setToken(userToken);
        token.setExpiration_date(jwtUtil.extractExpiration(userToken));
        token.setUser(user);
        Token savedToken = this.tokenRespository.save(token);

        user.setToken(savedToken);
        this.userRepository.save(user);

        return savedToken;
    }


}

