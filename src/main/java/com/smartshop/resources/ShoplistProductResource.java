package com.smartshop.resources;

import com.smartshop.dto.ProductShoplistDto;
import com.smartshop.dtoMappers.ProductShoplistMapper;
import com.smartshop.models.Product;
import com.smartshop.models.ProductShoplist;
import com.smartshop.models.Shoplist;
import com.smartshop.models.requestBody.EntityID;
import com.smartshop.models.requestBody.ProductAndPrice;
import com.smartshop.models.requestBody.ProductQuantity;
import com.smartshop.repositories.ProductRepository;
import com.smartshop.repositories.ShoplistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shoplists/{shoplist}/products")
public class ShoplistProductResource {

    private final ShoplistRepository shoplistRepository;

    private final ProductRepository productRepository;

    private final ProductShoplistMapper productShoplistMapper;

    private final Logger logger = LoggerFactory.getLogger(ShoplistProductResource.class);

    public ShoplistProductResource(ShoplistRepository shoplistRepository,
                                   ProductRepository productRepository,
                                   ProductShoplistMapper productShoplistMapper) {

        this.shoplistRepository = shoplistRepository;
        this.productRepository = productRepository;
        this.productShoplistMapper = productShoplistMapper;
    }


    @GetMapping
    public ResponseEntity<List<ProductShoplistDto>> index(
            @PathVariable("shoplist") Long id
    ) {
        Optional<Shoplist> shoplist = this.shoplistRepository.findById(id);
        if(shoplist.isEmpty()) return ResponseEntity.notFound().build();

        List<ProductShoplistDto> products =  shoplist.get().getProducts()
                .stream()
                .map(productShoplistMapper::toDto)
                .collect(Collectors.toList());


        return ResponseEntity.ok(products);
    }


    @PostMapping
    public ResponseEntity<?> store(
            @PathVariable("shoplist") Long id,
            @RequestBody EntityID entity
    ) {

        Optional<Shoplist> shoplist = this.shoplistRepository.findById(id);
        Optional<Product> product = this.productRepository.findById(entity.getId());

        if(shoplist.isEmpty() || product.isEmpty()) return ResponseEntity.notFound().build();

        // check if the product is already in the list, if true increment the quantity by 1
        long containsProduct = shoplist.get().getProducts().stream()
                .filter(ps -> ps.getProduct().getId() == product.get().getId())
                .count();

        if(containsProduct > 0) {

            int quantity = shoplist.get().getProducts()
                    .stream()
                    .filter(ps -> ps.getProduct().getId() == product.get().getId())
                    .map(ProductShoplist::getQuantity)
                    .findFirst()
                    .orElse(1) + 1;

            this.shoplistRepository.updateQuantity(
                    product.get().getId(),
                    shoplist.get().getId(),
                    quantity
                    );

            return ResponseEntity.ok(
                    this.productShoplistMapper.toDto(
                            new ProductShoplist(product.get(), shoplist.get(), quantity)
                    )
            );
        }

        ProductShoplist ps = new ProductShoplist();
        ps.setProduct(product.get());
        ps.setShoplist(shoplist.get());

        shoplist.get().getProducts().add(ps);
        product.get().getShoplists().add(ps);

        this.shoplistRepository.flush();
        ProductShoplistDto result = this.productShoplistMapper.toDto(ps);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);

    }

    @PutMapping("/{product}")
    public ResponseEntity<?> update(
            @PathVariable("shoplist") Long shoplistId,
            @PathVariable("product") Long productId,
            @RequestBody ProductQuantity p
            ) {
        Optional<Shoplist> shoplist = this.shoplistRepository.findById(shoplistId);
        Optional<Product> product = this.productRepository.findById(productId);
        if(! (shoplist.isPresent() && product.isPresent()) ) return ResponseEntity.notFound().build();

        this.shoplistRepository.updateQuantity(
                productId,
                shoplistId,
                p.getQuantity()
        );

        shoplist = this.shoplistRepository.findById(shoplistId);

        ProductShoplistDto products =  shoplist.get().getProducts()
                .stream()
                .map(productShoplistMapper::toDto)
                .collect(Collectors.toList())
                .get(0);

        return ResponseEntity.ok(products);
    }


    @DeleteMapping("/{product}")
    public ResponseEntity destroy(
            @PathVariable("shoplist") Long shoplistId,
            @PathVariable("product") Long productId
    ) {

        Optional<Shoplist> shoplist = this.shoplistRepository.findById(shoplistId);
        Optional<Product> product = this.productRepository.findById(productId);

        if(! (shoplist.isPresent() && product.isPresent()) ) return ResponseEntity.notFound().build();

        for(ProductShoplist p : shoplist.get().getProducts()) {
            if(productId.equals(p.getProduct().getId())) {
                shoplist.get().getProducts().remove(p);
                product.get().getShoplists().remove(p);
                this.shoplistRepository.saveAndFlush(shoplist.get());
            }
        }

        return ResponseEntity.noContent().build();
    }


}
