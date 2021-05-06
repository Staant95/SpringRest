package com.smartshop.models.responses;

import java.text.DecimalFormat;

public class SupermarketResponse {

    private final Long supermarket_id;

    private final Double longitude;

    private final Double latitude;

    private final String name;

    private final double total;

    private final String distanceFromSource;

    public SupermarketResponse(
            Long supermarket_id,
            Double longitude,
            Double latitude,
            String name,
            double total,
            double distanceFromSource
    ) {
        this.supermarket_id = supermarket_id;
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
        DecimalFormat df = new DecimalFormat("###.##");
        this.total = Double.parseDouble(df.format(total));
        this.distanceFromSource = df.format(distanceFromSource) + "km";
    }

    public Long getSupermarket_id() {
        return supermarket_id;
    }

    public Double getLongitude() {
        return longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public String getName() {
        return name;
    }

    public double getTotal() {
        return total;
    }

    public String getDistanceFromSource() {
        return distanceFromSource;
    }
}
