package com.smartshop.seeders;

import com.smartshop.models.Supermarket;
import com.smartshop.repositories.SupermarketRepository;
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

        supermarketList.add(new Supermarket("Conad"));
        supermarketList.add(new Supermarket("Carrefour"));

        this.supermarketRepository.saveAll(supermarketList);

    }
}
