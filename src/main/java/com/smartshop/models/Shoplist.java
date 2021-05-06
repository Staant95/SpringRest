package com.smartshop.models;


import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property  = "id", scope = Shoplist.class)
public class Shoplist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private String name;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnoreProperties("shoplists")
    private Set<User> users = new HashSet<>();


    @OneToMany(
            mappedBy = "shoplist",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference("shoplistProducts")
    private Set<ProductShoplist> products = new HashSet<>();


    public Shoplist(String name) {
        this.name = name;
    }


    public Shoplist() {
    }

    public Shoplist(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Set<User> getUsers() {
        return users;
    }

    public Set<ProductShoplist> getProducts() {
        return products;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public void setProducts(Set<ProductShoplist> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shoplist shoplist = (Shoplist) o;
        return getId().equals(shoplist.getId()) &&
                Objects.equals(getName(), shoplist.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }
}
