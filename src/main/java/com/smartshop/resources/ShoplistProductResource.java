package com.smartshop.resources;

import com.smartshop.dto.ProductShoplistDto;
import com.smartshop.dtoMappers.ProductShoplistMapper;
import com.smartshop.models.*;
import com.smartshop.models.requestBody.EntityID;
import com.smartshop.models.requestBody.ProductQuantity;
import com.smartshop.repositories.ProductRepository;
import com.smartshop.repositories.ShoplistRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

@RestController
@RequestMapping("/shoplists/{shoplist}/products")
public class ShoplistProductResource {

    private final Logger log = LoggerFactory.getLogger(ShoplistProductResource.class);

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
    public ResponseEntity<ProductShoplistDto> store(
            @PathVariable("shoplist") Long id,
            @Valid @RequestBody EntityID productId
    ) {
        log.info("ADDING PRODUCT WITH ID > " + productId.getId());
        Optional<Shoplist> shoplist = this.shoplistRepository.findById(id);
        Optional<Product> product = this.productRepository.findById(productId.getId());

        if(shoplist.isEmpty() || product.isEmpty()) return ResponseEntity.badRequest().build();

        // get, if exists, the product in the shopping list
        Optional<ProductShoplist> optionalProductInList = shoplist.get().getProducts().stream()
                .filter(ps -> ps.getProduct().getId().equals(productId.getId()))
                .findFirst();

        // if the product is in the list then increment by 1
        if(optionalProductInList.isPresent()) {
            int currentQuantity = optionalProductInList.get().getQuantity();
            ProductShoplist ps = optionalProductInList.get();

            ps.setQuantity(currentQuantity + 1);
            this.shoplistRepository.flush();

            return ResponseEntity.ok(this.productShoplistMapper.toDto(ps));
        }


        ProductShoplist ps = new ProductShoplist();

        ps.setShoplist(shoplist.get());
        ps.setProduct(product.get());

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

        Optional<ProductShoplist> result = shoplist.get().getProducts().stream()
                .filter(ps -> ps.getProduct().getId().equals(productId))
                .findFirst();

        if(result.isEmpty()) return ResponseEntity.notFound().build();

        result.get().setQuantity(p.getQuantity());
        this.shoplistRepository.saveAndFlush(result.get().getShoplist());

        return ResponseEntity.ok(
                this.productShoplistMapper.toDto(result.get())
        );

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
