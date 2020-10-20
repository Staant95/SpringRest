package com.smartshop.resources;

import com.smartshop.dto.ProductDto;
import com.smartshop.dto.ProductShoplistDto;
import com.smartshop.dtoMappers.ProductMapper;
import com.smartshop.dtoMappers.ProductShoplistMapper;
import com.smartshop.models.Product;
import com.smartshop.models.ProductShoplist;
import com.smartshop.models.Shoplist;
import com.smartshop.models.requestBody.ProductId;
import com.smartshop.models.requestBody.ProductQuantity;
import com.smartshop.repositories.ProductRepository;
import com.smartshop.repositories.ShoplistRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shoplists/{shoplist}/products")
public class ShoplistProductResource {

    @Autowired
    private ShoplistRepository shoplistRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductShoplistMapper productShoplistMapper;


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
    public ResponseEntity store(
            @PathVariable("shoplist") Long id,
            @RequestBody ProductId productId
    ) {

        Optional<Shoplist> shoplist = this.shoplistRepository.findById(id);
        Optional<Product> product = this.productRepository.findById(productId.getProductId());
        if(! (shoplist.isPresent() && product.isPresent()) ) return ResponseEntity.notFound().build();

        ProductShoplist ps = new ProductShoplist();
        ps.setProduct(product.get());
        ps.setShoplist(shoplist.get());

        shoplist.get().getProducts().add(ps);
        product.get().getShoplists().add(ps);

        this.shoplistRepository.flush();

        ProductShoplistDto products =  shoplist.get().getProducts()
                .stream()
                .filter(el -> el.getProduct().getId().equals(product.get().getId()))
                .map(productShoplistMapper::toDto)
                .collect(Collectors.toList())
                .get(0);

        return ResponseEntity.status(HttpStatus.CREATED).body(products);

    }

    @PutMapping("/{product}")
    public ResponseEntity update(
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

        return ResponseEntity.ok(null);
    }


}
