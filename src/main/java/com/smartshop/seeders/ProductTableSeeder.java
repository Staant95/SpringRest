package com.smartshop.seeders;

import com.github.javafaker.Faker;
import com.smartshop.models.Product;
import com.smartshop.repositories.ProductRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ProductTableSeeder implements Seeder {

    private final ProductRepository productRepository;

    private final Faker faker;

    public ProductTableSeeder(ProductRepository productRepository) {
        this.productRepository = productRepository;
        this.faker = new Faker();
    }


    @Override
    public void run() {

        List<Product> productList = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            productList.add(new Product(this.faker.food().vegetable()));
        }

        for (int i = 0; i < 5; i++) {
            productList.add(new Product(this.faker.food().fruit()));
        }

        for (int i = 0; i < 5; i++) {
            productList.add(new Product(this.faker.food().ingredient()));
        }

        this.productRepository.saveAll(productList);

    }
}
