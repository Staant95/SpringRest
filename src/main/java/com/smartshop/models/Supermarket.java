package com.smartshop.models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
@Setter
@Getter
@NoArgsConstructor
public class Supermarket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;


    @JsonManagedReference("supermarketProducts")
    @OneToMany(
            mappedBy = "supermarket",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<ProductSupermarket> products = new HashSet<>();



    public Supermarket(String name, double latitude, double longitude) {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Supermarket that = (Supermarket) o;
        return getId().equals(that.getId()) &&
                Objects.equals(getName(), that.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName());
    }


    public void addProduct(Product product, double price) {
        ProductSupermarket ps = new ProductSupermarket(product, this, price );
        this.products.add(ps);
        product.getSupermarkets().add(ps);
    }

    public void removeProduct(Product product) {

        ProductSupermarket ps = new ProductSupermarket(product, this);
        product.getSupermarkets().remove(ps);
        this.products.remove(ps);
        ps.setSupermarket(null);
        ps.setProduct(null);
    }

}
