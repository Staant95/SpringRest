package com.smartshop.resources;

import com.smartshop.dto.ProductSupermarketDto;
import com.smartshop.dtoMappers.ProductMapper;
import com.smartshop.dtoMappers.ProductSupermarketMapper;
import com.smartshop.models.Product;
import com.smartshop.models.ProductSupermarket;
import com.smartshop.models.Supermarket;
import com.smartshop.models.requestBody.ProductAndPrice;
import com.smartshop.repositories.ProductRepository;
import com.smartshop.repositories.SupermarketRepository;
import com.smartshop.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("supermarkets/{supermarket}/products")
public class SupermarketProductsResource {

    @Autowired
    private SupermarketRepository supermarketRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductSupermarketMapper productSupermarketMapper;

    @Autowired
    private ProductMapper productMapper;

    @GetMapping
    public ResponseEntity<List<ProductSupermarketDto>> index(
            @PathVariable("supermarket") Long supermarketId
    ) {

        Optional<Supermarket> searchedSupermarket = this.supermarketRepository.findById(supermarketId);

        if(searchedSupermarket.isEmpty()) return ResponseEntity.notFound().build();

        List<ProductSupermarketDto> products = searchedSupermarket.get().getProducts()
                                .stream()
                                .map(productSupermarketMapper::toDto)
                                .collect(Collectors.toList());

        return ResponseEntity.ok(products);
    }


    @PostMapping
    public ResponseEntity<?> store(
            @PathVariable("supermarket") Long supermarketId,
            @RequestBody ProductAndPrice product) {

        if(product.getPrice() == 0.0)
            return ResponseEntity.badRequest().body(new ResponseMessage("Price is required"));

        Optional<Supermarket> searchedSupermarket = this.supermarketRepository.findById(supermarketId);

        Optional<Product> searchedProduct = this.productRepository.findById(product.getProductId());

        if(!(searchedSupermarket.isPresent() && searchedProduct.isPresent()))
            return ResponseEntity.notFound().build();

        ProductSupermarket ps = new ProductSupermarket(
                searchedProduct.get(),
                searchedSupermarket.get(),
                product.getPrice()
        );
        searchedSupermarket.get().getProducts().add(ps);
        searchedProduct.get().getSupermarkets().add(ps);

        this.productRepository.flush();

        ProductSupermarketDto result = searchedSupermarket.get().getProducts()
                .stream()
                .filter(p -> product.getProductId().equals(p.getProduct().getId()))
                .map(productSupermarketMapper::toDto)
                .collect(Collectors.toList())
                .get(0); // ho per forza l'elemento aggiunto

        return ResponseEntity.ok(result);

    }

    @GetMapping("/{product}")
    public ResponseEntity<?> show(
            @PathVariable("supermarket") Long supermarketId,
            @PathVariable("product") Long productId
    ) {

        Optional<Supermarket> searchedSupermarket = this.supermarketRepository.findById(supermarketId);

        if(searchedSupermarket.isEmpty()) return ResponseEntity.notFound().build();

        List<ProductSupermarketDto> product = searchedSupermarket.get().getProducts()
                        .stream()
                        .filter(prod -> productId.equals(prod.getProduct().getId()))
                        .map(productSupermarketMapper::toDto)
                        .collect(Collectors.toList());

        if(product.size() != 1) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(product.get(0));
    }


    @DeleteMapping("/{product}")
    public ResponseEntity<?> destroy(
            @PathVariable("supermarket") Long supermarketId,
            @PathVariable("product") Long productId
    ) {

        Optional<Supermarket> searchedSupermarket = this.supermarketRepository.findById(supermarketId);
        this.productRepository.findById(productId); // is required

        if(searchedSupermarket.isEmpty()) return ResponseEntity.notFound().build();

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
