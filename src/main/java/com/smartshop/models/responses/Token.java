package com.smartshop.models.responses;


import lombok.Data;

import java.util.Date;

@Data
public class Token {
    private String token;

    private Date expiration_date;


    public Token(String jwt, Date expiration_date) {
        this.token = jwt;
        this.expiration_date = expiration_date;
    }

}
