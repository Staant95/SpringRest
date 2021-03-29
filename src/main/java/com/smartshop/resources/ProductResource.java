package com.smartshop.resources;

import com.smartshop.dto.ProductDto;
import com.smartshop.dtoMappers.ProductMapper;
import com.smartshop.models.Product;
import com.smartshop.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductResource {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductMapper productMapper;


    @GetMapping
    public ResponseEntity<List<ProductDto>> index() {
        return ResponseEntity.ok(
                productMapper.toDtoList(this.productRepository.findAll())
        );
    }

    @PostMapping
    public ResponseEntity<ProductDto> store(@Valid @RequestBody ProductDto product) {

        Product p = productMapper.fromDto(product);
        ProductDto saved = productMapper.toDto(this.productRepository.save(p));

        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }


    @GetMapping("/{product}")
    public ResponseEntity<ProductDto> show(@PathVariable("product") Long productId) {
        Optional<Product> product = this.productRepository.findById(productId);

        if(product.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(productMapper.toDto(product.get()));
    }

    @DeleteMapping("/{product}")
    public ResponseEntity<HttpStatus> destroy(@PathVariable("product") Long productId) {
        Optional<Product> product = this.productRepository.findById(productId);

        if(product.isEmpty()) return ResponseEntity.notFound().build();

        this.productRepository.delete(product.get());

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/search")
    public ResponseEntity<List<ProductDto>> search(@RequestParam("name") String name) {

        if(name.trim().length() == 0) return ResponseEntity.ok(List.of());

        Optional<List<Product>> matches = this.productRepository.findByNameStartsWith(name.trim());

        if(matches.isEmpty()) return ResponseEntity.ok(List.of());

        return ResponseEntity.ok(
                productMapper.toDtoList(matches.get())
        );

    }

}
