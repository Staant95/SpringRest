package com.smartshop.models;

import com.fasterxml.jackson.annotation.*;
import lombok.*;
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


    public Product(String name) {
        this.name = name;
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


//    @PreRemove
//    public void removeProductFromSupermarket() {
//        for(Supermarket s: supermarkets) {
//            s.getProducts().remove(this);
//        }
//    }

}
