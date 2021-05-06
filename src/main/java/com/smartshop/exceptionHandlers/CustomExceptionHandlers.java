package com.smartshop.exceptionHandlers;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class CustomExceptionHandlers extends ResponseEntityExceptionHandler {


    @ExceptionHandler(InvalidLoginCredentialsException.class)
    public ResponseEntity<Object> handleInvalidLoginCredentials(
            InvalidLoginCredentialsException ex,
            WebRequest request
    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(map);
    }

    @ExceptionHandler(DuplicateEmailException.class)
    @ResponseBody
    public ResponseEntity<Object> handleDuplicateEmailViolation(
            DuplicateEmailException ex,
            WebRequest request
    ) {

        Map<String, Object> map = new HashMap<>();
        map.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(map);
    }



    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            @NotNull HttpHeaders headers,
            @NotNull HttpStatus status,
            @NotNull WebRequest request) {

        List<FieldErrorResource> errorResources =
                ex.getBindingResult().getFieldErrors().stream()
                        .map(fieldError -> new FieldErrorResource(
                                            fieldError.getObjectName(),
                                            fieldError.getField(),
                                            fieldError.getCode(),
                                            fieldError.getDefaultMessage())
                        )
                        .collect(Collectors.toList());

        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorResource(errorResources));
    }


    @ExceptionHandler(value = {UsernameNotFoundException.class})
    protected ResponseEntity<?> handleUsernameNotFoundException(UsernameNotFoundException ex) {

        Map<String, Object> map = new HashMap<>();
        map.put("message", ex.getMessage());

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(map);
    }


}
