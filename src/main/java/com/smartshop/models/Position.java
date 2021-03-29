package com.smartshop.models;

import lombok.*;

import javax.validation.constraints.NotNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Position {

    @NotNull
    private Double latitude;

    @NotNull
    private Double longitude;

    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }
}
