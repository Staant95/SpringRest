package com.smartshop.resources;

import com.smartshop.dto.ProductSupermarketDto;
import com.smartshop.dtoMappers.ProductSupermarketMapper;
import com.smartshop.models.Product;
import com.smartshop.models.ProductSupermarket;
import com.smartshop.models.Supermarket;
import com.smartshop.models.requestBody.ProductInSupermarket;
import com.smartshop.repositories.ProductRepository;
import com.smartshop.repositories.SupermarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("supermarkets/{supermarket}/products")
public class SupermarketProductsResource {

    private final SupermarketRepository supermarketRepository;

    private final ProductRepository productRepository;

    private final ProductSupermarketMapper productSupermarketMapper;

    @Autowired
    public SupermarketProductsResource(SupermarketRepository supermarketRepository,
                                       ProductRepository productRepository,
                                       ProductSupermarketMapper productSupermarketMapper) {

        this.supermarketRepository = supermarketRepository;
        this.productRepository = productRepository;
        this.productSupermarketMapper = productSupermarketMapper;
    }


    @GetMapping
    public ResponseEntity<List<ProductSupermarketDto>> index(
            @PathVariable("supermarket") Long supermarketId
    ) {

        Supermarket searchedSupermarket = this.supermarketRepository.findById(supermarketId)
                .orElseThrow(EntityNotFoundException::new);

        List<ProductSupermarketDto> products = searchedSupermarket.getProducts()
                                .stream()
                                .map(productSupermarketMapper::toDto)
                                .collect(Collectors.toList());

        return ResponseEntity.ok(products);
    }


    @PostMapping
    public ResponseEntity<?> store(
            @PathVariable("supermarket") Long supermarketId,
            @Valid @RequestBody ProductInSupermarket product) {

        Optional<Supermarket> optionalSupermarket = this.supermarketRepository.findById(supermarketId);

        Optional<Product> optionalProduct = this.productRepository.findById(product.getProductId());

        if(optionalSupermarket.isEmpty() || optionalProduct.isEmpty())
            return ResponseEntity.notFound().build();

        ProductSupermarket ps = new ProductSupermarket(
                optionalProduct.get(),
                optionalSupermarket.get(),
                product.getPrice()
        );
        optionalSupermarket.get().getProducts().add(ps);
        optionalProduct.get().getSupermarkets().add(ps);

        this.productRepository.flush();

        ProductSupermarketDto result = productSupermarketMapper.toDto(ps);

        return ResponseEntity.ok(result);

    }

    @GetMapping("/{product}")
    public ResponseEntity<?> show(
            @PathVariable("supermarket") Long supermarketId,
            @PathVariable("product") Long productId
    ) {

        Optional<Supermarket> searchedSupermarket = this.supermarketRepository.findById(supermarketId);
        Optional<Product> optionalProduct = this.productRepository.findById(productId);

        if(searchedSupermarket.isEmpty() || optionalProduct.isEmpty()) return ResponseEntity.notFound().build();

        Optional<ProductSupermarketDto> product = searchedSupermarket.get().getProducts()
                        .stream()
                        .filter(productSupermarket -> productId.equals(productSupermarket.getProduct().getId()))
                        .map(productSupermarketMapper::toDto)
                        .findFirst();

        if(product.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(product.get());
    }


    @DeleteMapping("/{product}")
    public ResponseEntity<?> destroy(
            @PathVariable("supermarket") Long supermarketId,
            @PathVariable("product") Long productId
    ) {

        Optional<Supermarket> searchedSupermarket = this.supermarketRepository.findById(supermarketId);
        Optional<Product> optionalProduct = this.productRepository.findById(productId);

        if(searchedSupermarket.isEmpty() || optionalProduct.isEmpty()) return ResponseEntity.notFound().build();

        for(ProductSupermarket p: searchedSupermarket.get().getProducts()) {

            if(productId.equals(p.getProduct().getId())) {
               searchedSupermarket.get().removeProduct(p.getProduct());
               Supermarket supermarket = searchedSupermarket.get();
               this.supermarketRepository.saveAndFlush(supermarket);
            }
        }

        return ResponseEntity.noContent().build();
    }


}
