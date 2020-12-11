package com.smartshop.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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

    @JsonManagedReference("supermarketProducts")
    @OneToMany(
            mappedBy = "supermarket",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private Set<ProductSupermarket> products = new HashSet<>();



    public Supermarket(String name) {
        this.name = name;
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
