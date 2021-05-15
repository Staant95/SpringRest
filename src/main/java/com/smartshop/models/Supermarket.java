package com.smartshop.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Point;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


@Entity
public class Supermarket {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    @Column(nullable = false)
    private double latitude;

    @Column(nullable = false)
    private double longitude;

    @Column(columnDefinition = "geometry")
    private Point location;


    @JsonManagedReference("supermarketProducts")
    @OneToMany(
            mappedBy = "supermarket",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private final Set<ProductSupermarket> products = new HashSet<>();


    public Supermarket(String name, Point location) {
        this.name = name;
        this.location = location;
    }
    public Coordinate getLocation() {
        return location.getCoordinate();
    }

    public void setLocation(Point location) {
        this.location = location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Supermarket() {
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Set<ProductSupermarket> getProducts() {
        return products;
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
