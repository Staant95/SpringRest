package com.smartshop.seeders;

import com.smartshop.models.Product;
import com.smartshop.models.ProductSupermarket;
import com.smartshop.models.Supermarket;
import com.smartshop.repositories.ProductRepository;
import com.smartshop.repositories.SupermarketRepository;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Random;

@Component
@Transactional
public class ProductSupermarketTableSeeder implements Seeder {

    private final SupermarketRepository supermarketRepository;

    private final ProductRepository productRepository;


    public ProductSupermarketTableSeeder(
            SupermarketRepository supermarketRepository,
            ProductRepository productRepository
    ) {
        this.supermarketRepository = supermarketRepository;
        this.productRepository = productRepository;
    }



    @Override
    public void run() {

        List<Supermarket> supermarkets = this.supermarketRepository.findAll();
        List<Product> products = this.productRepository.findAll();

        for(Supermarket s : supermarkets) {

            for(Product p : products) {

                Supermarket tempSupermarket = this.supermarketRepository.findById(s.getId()).get();

                Product tempProd = this.productRepository.findById(p.getId()).get();

                double randomPrice = new Random().nextDouble() * 20.0 + 0.50;
                randomPrice = Math.round(randomPrice * 100.0) / 100.0;

                ProductSupermarket ps = new ProductSupermarket(tempProd, tempSupermarket, randomPrice );

                tempSupermarket.getProducts().add(ps);
                tempProd.getSupermarkets().add(ps);

                this.supermarketRepository.flush();

            }


        }
    }

}
