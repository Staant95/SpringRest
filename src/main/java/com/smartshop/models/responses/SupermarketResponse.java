package com.smartshop.models.responses;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.text.DecimalFormat;
@Getter
@Setter
@NoArgsConstructor
public class SupermarketResponse {

    private Long supermarket_id;

    private Double longitude;

    private Double latitude;

    private String name;

    private double total;

    public SupermarketResponse(Long supermarket_id, Double longitude, Double latitude, String name, double total) {
        this.supermarket_id = supermarket_id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        DecimalFormat df = new DecimalFormat("###.##");
        this.total = Double.parseDouble(df.format(total));
    }
}
