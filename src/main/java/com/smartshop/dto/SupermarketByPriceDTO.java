package com.smartshop.dto;

import com.smartshop.models.Supermarket;

import java.text.DecimalFormat;

public class SupermarketByPriceDTO {
    
    private double total;

    private String latitude;

    private String longitude;

    private String name;

    public SupermarketByPriceDTO(double total, Supermarket supermarket) {
        DecimalFormat df = new DecimalFormat("###.##");
        this.total = Double.parseDouble(df.format(total));
        this.name = supermarket.getName();
        this.latitude = String.valueOf(supermarket.getLocation().x);
        this.longitude = String.valueOf(supermarket.getLocation().y);
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
}
