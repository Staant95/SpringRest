package com.smartshop.models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductShoplist implements Serializable {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("productShoplist")
    public Product product;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonBackReference("shoplistProducts")
    public Shoplist shoplist;


    private int quantity = 1;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductShoplist that = (ProductShoplist) o;
        return getProduct().equals(that.getProduct()) &&
                getShoplist().equals(that.getShoplist());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getProduct(), getShoplist());
    }
}
