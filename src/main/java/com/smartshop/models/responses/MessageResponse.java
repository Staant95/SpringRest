package com.smartshop.models.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
public class MessageResponse implements Serializable {

    private String message;

    private Map<String, String> errors;


    public MessageResponse(String message) {
        this.message = message;
    }
}
