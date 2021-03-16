package com.smartshop.seeders;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class DatabaseSeeder {
    private final List<Seeder> seederList = new ArrayList<>();
    
    private UserTableSeeder userTableSeeder;
    private SupermarketTableSeeder supermarketTableSeeder;
    private ProductTableSeeder productTableSeeder;
    private ProductSupermarketTableSeeder psTableSeeder;
    private ShoplistProductTableSeeder spSeeder;

    public DatabaseSeeder(UserTableSeeder userTableSeeder,
                          SupermarketTableSeeder supermarketTableSeeder,
                          ProductTableSeeder productTableSeeder,
                          ProductSupermarketTableSeeder psTableSeeder,
                          ShoplistProductTableSeeder spSeeder) {

        this.userTableSeeder = userTableSeeder;
        this.supermarketTableSeeder = supermarketTableSeeder;
        this.productTableSeeder = productTableSeeder;
        this.psTableSeeder = psTableSeeder;
        this.spSeeder = spSeeder;


        // order matters
        seederList.add(this.supermarketTableSeeder);
        seederList.add(this.productTableSeeder);
        seederList.add(this.userTableSeeder);
        seederList.add(this.psTableSeeder);
        seederList.add(this.spSeeder);

    }


    public void seed() {

        seederList.forEach(Seeder::run);

    }
}
