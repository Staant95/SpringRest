package com.smartshop.exceptionHandlers;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class FieldErrorResource {
    
    private String resource;

    private String field;

    private String code;

    private String message;

}
