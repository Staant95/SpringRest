package com.smartshop.services;

import com.smartshop.models.Position;
import org.springframework.stereotype.Service;

@Service
public class DistanceBetweenTwoPoints {

    public double calculateInKm(Position user, Position supermarket) {
        final int R = 6371; // Radious of the earth

        double latDistance = toRad(supermarket.getLatitude() - user.getLatitude());
        double lonDistance = toRad(supermarket.getLongitude() - user.getLatitude());

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(toRad(user.getLatitude())) * Math.cos(toRad(supermarket.getLatitude()))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);


        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }


    private double toRad(double value) {
        return value * Math.PI / 180;
    }
}
