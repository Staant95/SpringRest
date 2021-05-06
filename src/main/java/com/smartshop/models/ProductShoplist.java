package com.smartshop.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
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

    public ProductShoplist() {
    }

    public ProductShoplist(Product product, Shoplist shoplist) {
        this.product = product;
        this.shoplist = shoplist;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Shoplist getShoplist() {
        return shoplist;
    }

    public void setShoplist(Shoplist shoplist) {
        this.shoplist = shoplist;
    }

    public int getQuantity() {
        return quantity;
    }

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
