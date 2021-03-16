package com.smartshop.models.responses;


import com.smartshop.dto.UserDto;
import com.smartshop.models.User;
import lombok.Data;

import java.util.Date;

@Data
public class Token {
    private String token;

    private Date expiration_date;

    private UserDto user;


    public Token(String jwt, Date expiration_date) {
        this.token = jwt;
        this.expiration_date = expiration_date;
    }

    public Token(String jwt, Date expiration_date, UserDto user) {
        this.token = jwt;
        this.expiration_date = expiration_date;
        this.user = user;
    }
}
