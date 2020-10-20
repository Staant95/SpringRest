package com.smartshop.services;

import com.smartshop.models.Token;
import com.smartshop.models.User;
import com.smartshop.models.auth.AuthenticationRequest;
import com.smartshop.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    public boolean authenticateUser(AuthenticationRequest authenticationRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
        }
        catch (BadCredentialsException e) {
            return false;
        }
        return true;
    }


    public Token createToken(User user, UserDetails userDetails) {

        if(user.getToken() != null) {

            if(jwtUtil.validateToken(user.getToken().getToken(), userDetails)) {

                return user.getToken();
            }
            // token is invalid, delete from database and continue
            user.removeToken(user.getToken());
        }

        final String jwt = jwtUtil.generateToken(userDetails);

        return new Token(jwt, jwtUtil.extractExpiration(jwt), user);
    }

}
