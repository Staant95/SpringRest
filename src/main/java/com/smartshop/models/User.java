package com.smartshop.models;

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

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Email field is required")
    private String email;

    @Column(nullable = false)
    @NotBlank(message = "Password field is required")
    private String password;

    @Column
    private String role =  "USER";

    @OneToOne(mappedBy = "user")
    private Token token;

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {
                    CascadeType.PERSIST,
                    CascadeType.MERGE
            })
    @JoinTable(name = "user_shoplist",
            joinColumns = { @JoinColumn(name = "user_id") },
            inverseJoinColumns = { @JoinColumn(name = "shoplist_id") })
    private Set<Shoplist> shoplists = new HashSet<>();

}
