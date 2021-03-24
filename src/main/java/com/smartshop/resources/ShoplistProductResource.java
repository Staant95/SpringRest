package com.smartshop.resources;

import com.smartshop.dto.ProductShoplistDto;
import com.smartshop.dtoMappers.ProductShoplistMapper;
import com.smartshop.models.*;
import com.smartshop.models.requestBody.EntityID;
import com.smartshop.models.requestBody.ProductQuantity;
import com.smartshop.repositories.ProductRepository;
import com.smartshop.repositories.ShoplistRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shoplists/{shoplist}/products")
@Slf4j
public class ShoplistProductResource {

    private final ShoplistRepository shoplistRepository;

    private final ProductRepository productRepository;

    private final ProductShoplistMapper productShoplistMapper;


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
            @RequestBody EntityID productId
    ) {

        Optional<Shoplist> shoplist = this.shoplistRepository.findById(id);
        Optional<Product> product = this.productRepository.findById(productId.getId());

        if(shoplist.isEmpty() || product.isEmpty()) return ResponseEntity.notFound().build();

        // check if the product is already in the list, if true increment the quantity by 1
        // one query for each product in the list..

        Integer productCount = this.shoplistRepository.containsProduct(shoplist.get().getId(), productId.getId());

        if(productCount > 0) {
            int quantity = this.shoplistRepository.getProductQuantity(shoplist.get().getId(), productId.getId()) + 1;

            this.shoplistRepository.updateQuantity(
                    product.get().getId(),
                    shoplist.get().getId(),
                    quantity
            );

            ProductShoplistDto result = this.productShoplistMapper.toDto(new ProductShoplist(
                    product.get(),
                    shoplist.get(),
                    quantity
            ));

            return ResponseEntity.ok(result);
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

        if(shoplist.isEmpty() || product.isEmpty() ) return ResponseEntity.notFound().build();

        this.shoplistRepository.updateQuantity(
                productId,
                shoplistId,
                p.getQuantity()
        );

        shoplist = this.shoplistRepository.findById(shoplistId);

        ProductShoplistDto products = shoplist.get().getProducts()
                .stream()
                .map(productShoplistMapper::toDto)
                .collect(Collectors.toList())
                .get(0);

        return ResponseEntity.ok(products);
    }


    @DeleteMapping("/{product}")
    public ResponseEntity<ResponseStatus> destroy(
            @PathVariable("shoplist") Long shoplistId,
            @PathVariable("product") Long productId
    ) {

        Optional<Shoplist> shoplist = this.shoplistRepository.findById(shoplistId);
        Optional<Product> product = this.productRepository.findById(productId);

        // fail fast
        if(shoplist.isEmpty() || product.isEmpty()) return ResponseEntity.notFound().build();

        Optional<ProductShoplist> productToDelete = shoplist.get().getProducts().stream()
                .filter(p -> productId.equals(p.getProduct().getId()))
                .findFirst();


        if(productToDelete.isEmpty()) return ResponseEntity.notFound().build();

        shoplist.get().getProducts().remove(productToDelete.get());
        product.get().getShoplists().remove(productToDelete.get());

        this.shoplistRepository.saveAndFlush(shoplist.get());

        return ResponseEntity.noContent().build();
    }


}
