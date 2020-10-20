package com.smartshop.exceptionHandlers;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@RestController
public class ConstraintViolationHandler extends ResponseEntityExceptionHandler {

    @Order(Ordered.HIGHEST_PRECEDENCE)
    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> handleConstraint(ConstraintViolationException ex,
                                                   WebRequest request ) {

        return new ResponseEntity(
                "Constraint violation happened",
                HttpStatus.BAD_REQUEST);
    }

}
