package com.smartshop.models.responses;

import com.smartshop.dto.UserDto;

import java.util.Date;

public class Token {
    private final String token;

    private final Date expiration_date;

    private final UserDto user;

    public Token(String jwt, Date expiration_date, UserDto user) {
        this.token = jwt;
        this.expiration_date = expiration_date;
        this.user = user;
    }

    public String getToken() {
        return token;
    }

    public Date getExpiration_date() {
        return expiration_date;
    }

    public UserDto getUser() {
        return user;
    }
}
