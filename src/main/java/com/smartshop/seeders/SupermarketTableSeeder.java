package com.smartshop.seeders;

import com.smartshop.models.Supermarket;
import com.smartshop.repositories.SupermarketRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SupermarketTableSeeder implements Seeder {


    private final SupermarketRepository supermarketRepository;


    public SupermarketTableSeeder(SupermarketRepository supermarketRepository) {
        this.supermarketRepository = supermarketRepository;
    }



    @Override
    public void run() {

        this.supermarketRepository.saveAll(List.of(
            new Supermarket("Conad", createPoint(42.4198, 12.9105)),
            new Supermarket("Carrefour", createPoint(42.4051, 12.8658))
        ));

    }


    private Point createPoint(double lat, double lon) {
        GeometryFactory factory = new GeometryFactory();
        return factory.createPoint(
                new Coordinate(lat, lon)
        );

    }
}
