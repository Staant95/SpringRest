package com.smartshop.services;

import com.smartshop.models.Position;
import com.smartshop.models.Supermarket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GeolocationService {


    public List<Supermarket> filterSupermarketsByDistance(Position user, List<Supermarket> supermarkets) {

         return supermarkets.stream()
                 .filter(supermarket -> isInRange(user, new Position(supermarket.getLatitude(), supermarket.getLongitude())))
                 .collect(Collectors.toList());

    }


    // Haversine Formula
    private boolean isInRange(Position source, Position destination) {
        final int R = 6373; // Radius of the earth

        double latitude = toRad(destination.getLatitude() - source.getLatitude());
        double longitude = toRad(destination.getLongitude() - source.getLongitude());

        double a = Math.sin(latitude / 2) * Math.sin(latitude / 2)
                + Math.cos(toRad(source.getLatitude())) * Math.cos(toRad(destination.getLatitude()))
                * Math.sin(longitude / 2) * Math.sin(longitude / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return (R * c) < source.getMaxDistance();
    }


    private double toRad(double value) {
        return value * Math.PI / 180;
    }
}
