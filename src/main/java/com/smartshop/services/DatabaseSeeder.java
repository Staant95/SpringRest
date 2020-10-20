package com.smartshop.services;

import com.smartshop.models.Product;
import com.smartshop.models.ProductSupermarket;
import com.smartshop.models.Supermarket;
import com.smartshop.repositories.ProductRepository;
import com.smartshop.repositories.SupermarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class DatabaseSeeder {

    @Autowired
    private SupermarketRepository supermarketRepository;

    @Autowired
    private ProductRepository productRepository;

    public void seed() {

        Supermarket supermarket1 = new Supermarket();
        Supermarket supermarket2 = new Supermarket();
        supermarket1.setName("Conad");
        supermarket2.setName("Carrefour");



        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();
        Product product4 = new Product();
        product1.setName("Pane");
        product2.setName("Carne");
        product3.setName("Mele");
        product4.setName("Latte");

        this.productRepository.saveAll(Arrays.asList(product1, product2, product3, product4));


        addProductToSupermarket(supermarket1, product1, 1.2);

        addProductToSupermarket(supermarket1, product2, 4.2);
        addProductToSupermarket(supermarket1, product3, 1.8);
        addProductToSupermarket(supermarket1, product4, 0.5);

        addProductToSupermarket(supermarket2, product1, 0.99);
        addProductToSupermarket(supermarket2, product2, 4.5);
        addProductToSupermarket(supermarket2, product3, 2.6);
        addProductToSupermarket(supermarket2, product4, 1.2);

        this.supermarketRepository.saveAll(Arrays.asList(supermarket1, supermarket2));


    }

    private void addProductToSupermarket(Supermarket supermarket1, Product product1, double price) {

        ProductSupermarket ps1 = new ProductSupermarket(product1, supermarket1,price );
        supermarket1.getProducts().add(ps1);
        product1.getSupermarkets().add(ps1);

    }


}
