package com.smartshop.models;

import com.fasterxml.jackson.annotation.*;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@NoArgsConstructor
@Setter
@Getter
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property  = "id", scope = User.class)
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

    @Column(nullable = false, unique = true)
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


    @ManyToMany(mappedBy = "users", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("users")
    private Set<Shoplist> shoplists = new HashSet<>();


    public User(@NotBlank(message = "Name field is required") String name, @NotBlank(message = "Lastname field is required") String lastname, @NotBlank(message = "Email field is required") String email, @NotBlank(message = "Password field is required") String password) {
        this.name = name;
        this.lastname = lastname;
        this.email = email;
        this.password = password;
    }

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


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getId().equals(user.getId()) &&
                getEmail().equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getEmail());
    }
}
