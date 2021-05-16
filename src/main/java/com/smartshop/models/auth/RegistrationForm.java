package com.smartshop.models.auth;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class RegistrationForm {

    @NotBlank(message = "Can't be empty")
    private String name;

    @NotBlank(message = "Can't be empty")
    private String lastname;

    @NotBlank(message = "Can't be empty")
    @Email(message = "Should be an email")
    private String email;

    @NotBlank(message = "Can't be empty")
    private String password;

    public RegistrationForm() {
    }

    public RegistrationForm(String name, String lastname, String email, String password) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getLastname() {
        return lastname;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

