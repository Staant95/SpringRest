package com.smartshop.dto;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.smartshop.models.User;

import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@JsonIdentityInfo(generator= ObjectIdGenerators.PropertyGenerator.class, property="id")
public class ShoplistDto implements Serializable {

    private Long id;
    private String name;
    private Set<UserDto> users;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<UserDto> getUsers() {
        return users;
    }

    public void setUsers(Set<UserDto> usersDto) {
        this.users = usersDto;
    }
}
