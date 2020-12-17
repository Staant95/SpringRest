package com.smartshop.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String token;

    private Date expiration_date;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @JsonBackReference
    private User user;


    public Token(String jwt, Date expiration_date, User user) {
        this.token = jwt;
        this.expiration_date = expiration_date;
        this.user = user;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Token token1 = (Token) o;
        return getId().equals(token1.getId()) &&
                Objects.equals(getToken(), token1.getToken()) &&
                Objects.equals(getExpiration_date(), token1.getExpiration_date());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getToken(), getExpiration_date());
    }
}
