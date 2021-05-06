package com.smartshop.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
public class ProductSupermarket implements Serializable {

    @Id
    @ManyToOne()
    @Fetch(FetchMode.JOIN)
    @JsonBackReference("productSupermarkets")
    private Product product;


    @Id
    @ManyToOne()
    @JsonBackReference("supermarketProducts")
    @Fetch(FetchMode.JOIN)
    private Supermarket supermarket;

    @Column(nullable = false)
    private double price;


    public ProductSupermarket(Product p, Supermarket s) {
        this.product = p;
        this.supermarket = s;
    }

    public ProductSupermarket(Product p, Supermarket s, double price) {
        this.product = p;
        this.supermarket = s;
        this.price = price;
    }


    public ProductSupermarket() {
    }

    public Product getProduct() {
        return product;
    }

    public Supermarket getSupermarket() {
        return supermarket;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductSupermarket that = (ProductSupermarket) o;
        return getProduct().equals(that.getProduct()) &&
                getSupermarket().equals(that.getSupermarket());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProduct(), getSupermarket());
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public void setSupermarket(Supermarket supermarket) {
        this.supermarket = supermarket;
    }
}
