package com.smartshop.auth.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartshop.auth.CustomUserDetailsService;
import com.smartshop.utils.JwtUtil;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtRequestFilter extends OncePerRequestFilter {

    private Logger log = LoggerFactory.getLogger(JwtRequestFilter.class);

    private CustomUserDetailsService userDetailsService;

    private JwtUtil jwtUtil;

    public JwtRequestFilter(CustomUserDetailsService userDetailsService, JwtUtil jwtUtil) {
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    public JwtRequestFilter() {}


    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            @NotNull HttpServletResponse response,
            @NotNull FilterChain chain) throws ServletException, IOException, AuthenticationException {

        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt;

        // Extract Token from header or else return 401
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);

            try {
                username = jwtUtil.extractUsername(jwt);
            } catch (IllegalArgumentException | ExpiredJwtException | SignatureException | MalformedJwtException e) {
                log.info("An error occured while validating token");
            }
        } else {
            log.info("Token is missing from request. URL: " + request.getRequestURI());
            response.setStatus(401);
            response.addHeader("Content-Type", "application/json");
            response.getWriter().write(
                    new ObjectMapper().writeValueAsString(new ErrorMessage(request.getRequestURI()))
            );
            return;
        }

        // Set context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (jwtUtil.validateToken(jwt, userDetails)) {

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities()
                );

                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        chain.doFilter(request, response);
    }

}

class ErrorMessage {
    String message = "You need to obtain a token before accessing ";

    public ErrorMessage(String path) {
        this.message += path;
    }

    public ErrorMessage() {
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}