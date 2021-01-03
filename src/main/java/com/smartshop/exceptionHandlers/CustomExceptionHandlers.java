package com.smartshop.exceptionHandlers;

import com.smartshop.models.responses.MessageResponse;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
@Slf4j
public class CustomExceptionHandlers extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraintViolation() {
        return new ResponseEntity(new MessageResponse("Email is already taken.spoti"),HttpStatus.CONFLICT);
    }



    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status,
                                                                  WebRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });


        return new ResponseEntity<>(new MessageResponse("Invalid data", errors), HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(value = {UsernameNotFoundException.class})
    protected ResponseEntity<?> handleUsernameNotFoundException() {
        return new ResponseEntity(new MessageResponse("Your token is invalid"), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {BadCredentialsException.class})
    protected ResponseEntity<?> handleBadCredentialsException() {
        return new ResponseEntity(new MessageResponse("Email or password are incorrect!"), HttpStatus.UNAUTHORIZED);
    }


}
