package com.smartshop.seeders;

import com.smartshop.models.Supermarket;
import com.smartshop.repositories.SupermarketRepository;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class SupermarketTableSeeder implements Seeder {


    private final SupermarketRepository supermarketRepository;


    public SupermarketTableSeeder(SupermarketRepository supermarketRepository) {
        this.supermarketRepository = supermarketRepository;
    }



    @Override
    public void run() {

        List<Supermarket> supermarketList = new ArrayList<>();
        supermarketList.add(new Supermarket("Conad", 42.405139, 12.865847));
        supermarketList.add(new Supermarket("Carrefour", 42.409771, 12.883291));

        this.supermarketRepository.saveAll(supermarketList);

    }
}
