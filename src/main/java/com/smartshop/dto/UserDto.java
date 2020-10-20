package com.smartshop.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import java.util.Set;

@Data
@JsonIgnoreProperties(value={ "password" }, allowSetters = true)
@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class UserDto {
    private Long id;
    private String name;
    private String lastname;
    private String email;
    private String password;


}
