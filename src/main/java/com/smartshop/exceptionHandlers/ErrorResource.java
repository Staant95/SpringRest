package com.smartshop.exceptionHandlers;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize(using = ErrorResourceSerializer.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonRootName("errors")
public class ErrorResource {
   private final List<FieldErrorResource> fieldErrors;
   
   public ErrorResource(List<FieldErrorResource> fieldErrorResources) {
       this.fieldErrors = fieldErrorResources;
   }

    public List<FieldErrorResource> getFieldErrors() {
        return fieldErrors;
    }

}
