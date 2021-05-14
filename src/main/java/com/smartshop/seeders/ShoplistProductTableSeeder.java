package com.smartshop.seeders;

import com.smartshop.models.Product;
import com.smartshop.models.ProductShoplist;
import com.smartshop.models.Shoplist;
import com.smartshop.repositories.ProductRepository;
import com.smartshop.repositories.ShoplistRepository;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.List;

@Component
@Transactional
public class ShoplistProductTableSeeder implements Seeder {

    private final ShoplistRepository shoplistRepository;

    private final ProductRepository productRepository;

    public ShoplistProductTableSeeder(ShoplistRepository shoplistRepository, ProductRepository productRepository) {
        this.shoplistRepository = shoplistRepository;
        this.productRepository = productRepository;
    }


    @Override
    public void run() {

        List<Shoplist> shoplists = this.shoplistRepository.findAll();
        List<Product> products = this.productRepository.findAll();

        int len = products.size();


        for(Shoplist s: shoplists) {

            int floor = (int)(Math.random() * ((len - (len / 2))));
            int ceil = len/2 + (int)(Math.random() * (len / 2));
            // randomly assign product to shoplist
            products.subList(floor, ceil).forEach(product -> {
                ProductShoplist ps = new ProductShoplist();
                ps.setProduct(product);
                ps.setShoplist(s);
                s.getProducts().add(ps);
                product.getShoplists().add(ps);

                this.shoplistRepository.flush();
            });
        }

    }
}
