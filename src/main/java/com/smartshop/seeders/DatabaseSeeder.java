package com.smartshop.services;

import com.smartshop.seeders.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseSeeder {

    private final List<Seeder> seederList = new ArrayList<>();


    public DatabaseSeeder(
            UserTableSeeder userTableSeeder,
            SupermarketTableSeeder supermarketTableSeeder,
            ProductTableSeeder productTableSeeder,
            ProductSupermarketTableSeeder psTableSeeder,
            ShoplistProductTableSeeder spSeeder
            ) {

        // order matters
        seederList.add(supermarketTableSeeder);
        seederList.add(productTableSeeder);
        seederList.add(userTableSeeder);
        seederList.add(psTableSeeder);
        seederList.add(spSeeder);

    }


    public void seed() {

        seederList.forEach(Seeder::run);

    }

}
