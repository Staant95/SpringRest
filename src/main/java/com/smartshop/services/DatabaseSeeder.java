package com.smartshop.services;

import com.smartshop.models.Product;
import com.smartshop.models.ProductSupermarket;
import com.smartshop.models.Supermarket;
import com.smartshop.repositories.ProductRepository;
import com.smartshop.repositories.SupermarketRepository;
import com.smartshop.repositories.UserRepository;
import com.smartshop.seeders.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseSeeder {

    private final SupermarketRepository supermarketRepository;

    private final ProductRepository productRepository;


    private final List<Seeder> seederList = new ArrayList<>();


    public DatabaseSeeder(
            SupermarketRepository supermarketRepository,
            ProductRepository productRepository,
            UserTableSeeder userTableSeeder,
            SupermarketTableSeeder supermarketTableSeeder,
            ProductTableSeeder productTableSeeder,
            ProductSupermarketTableSeeder psTableSeeder
    ) {
        this.supermarketRepository = supermarketRepository;
        this.productRepository = productRepository;

        // order matters
        seederList.add(supermarketTableSeeder);
        seederList.add(productTableSeeder);
        seederList.add(userTableSeeder);
        seederList.add(psTableSeeder);


    }



    public void seed() {

        for(Seeder s: seederList) {

            s.run();

        }

    }



}
