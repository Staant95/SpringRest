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
//        supermarketList.add(new Supermarket("Coop", 42.4080161, 12.8498633));
        supermarketList.add(new Supermarket("Conad", 42.419873, 12.910598));
        supermarketList.add(new Supermarket("Carrefour", 42.405185, 12.865871));

        this.supermarketRepository.saveAll(supermarketList);

    }
}
