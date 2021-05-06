package com.smartshop.models;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;


    @JsonManagedReference("productSupermarkets")
    @OneToMany(
            mappedBy = "supermarket",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<ProductSupermarket> supermarkets = new HashSet<>();


    @OneToMany(
            mappedBy = "shoplist",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference("productShoplist")
    private Set<ProductShoplist> shoplists = new HashSet<>();

    public Product() {
    }

    public Product(String name) {
        this.name = name;
    }

    public Product(Long id) {
        this.id = id;
    }

    public Product(Long id, String name) {
        this.id = id;
        this.name = name;
    }


    public void setName(String name) {
        this.name = name;
    }

    public void setSupermarkets(Set<ProductSupermarket> supermarkets) {
        this.supermarkets = supermarkets;
    }

    public void setShoplists(Set<ProductShoplist> shoplists) {
        this.shoplists = shoplists;
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

    public Set<ProductSupermarket> getSupermarkets() {
        return supermarkets;
    }

    public Set<ProductShoplist> getShoplists() {
        return shoplists;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return getId().equals(product.getId()) &&
                Objects.equals(getName(), product.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }


}
