package com.smartshop.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Set;

@Entity
@NoArgsConstructor
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "Name field is required")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Lastname field is required")
    private String lastname;

    @Column(nullable = false)
    @NotBlank(message = "Email field is required")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password field is required")
    private String password;

    private String role = "user";

    @OneToOne(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private Token token;


    public void addToken(Token token) {
        token.setUser(this);
        this.setToken(token);
    }

    public void removeToken(Token token) {
        if(token != null) {
            token.setUser(null);
            this.token = null;
        }
    }

}
