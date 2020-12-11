package com.smartshop.services;

import com.github.javafaker.Faker;
import com.smartshop.SmartshopApplication;
import com.smartshop.models.Product;
import com.smartshop.models.ProductSupermarket;
import com.smartshop.models.Supermarket;
import com.smartshop.models.User;
import com.smartshop.repositories.ProductRepository;
import com.smartshop.repositories.SupermarketRepository;
import com.smartshop.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class DatabaseSeeder {

    private final SupermarketRepository supermarketRepository;

    private final ProductRepository productRepository;

    private final Faker faker;

    private final UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    public DatabaseSeeder(SupermarketRepository supermarketRepository, ProductRepository productRepository, UserRepository userRepository) {
        this.supermarketRepository = supermarketRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.faker = new Faker();
    }


    // could be divided in sub classes
    public void seed() {

        // --------------- Supermarket ----------

        List<Supermarket> supermarketList = new ArrayList<>();

        supermarketList.add(new Supermarket("Conad"));
        supermarketList.add(new Supermarket("Carrefour"));

        // --------------- Products ------------

        List<Product> productList = new ArrayList<>();
        productList.add(new Product("Pane"));
        productList.add(new Product("Carne"));
        productList.add(new Product("Mele"));
        productList.add(new Product("Latte"));

        productRepository.saveAll(productList);



        for(Supermarket s : supermarketList) {

            for(Product p : productList) {

                double randomPrice = new Random().nextDouble() * 30.0 + 0.50;
                addProductToSupermarket(s, p, randomPrice);

            }

        }

        supermarketRepository.saveAll(supermarketList);



        // --------------- Users ------------

        for(int i = 0; i < 3; i++) {

            User u = new User(
                    faker.name().firstName(),
                    faker.name().lastName(),
                    faker.lorem().word() + "@gmail.com",
                    faker.lorem().word());

            logger.info("Email -> " + u.getEmail() + " password -> " + u.getPassword());

            u.setPassword(new BCryptPasswordEncoder().encode(u.getPassword()));
            this.userRepository.save(u);
        }


    }

    private void addProductToSupermarket(Supermarket supermarket1, Product product1, double price) {

        ProductSupermarket ps1 = new ProductSupermarket(product1, supermarket1,price );
        supermarket1.getProducts().add(ps1);
        product1.getSupermarkets().add(ps1);

    }


}
