package com.smartshop.resources;

import com.smartshop.dto.ShoplistDto;
import com.smartshop.dtoMappers.ShoplistMapper;
import com.smartshop.models.BestSupermarket;
import com.smartshop.models.Shoplist;
import com.smartshop.models.Supermarket;
import com.smartshop.repositories.ShoplistRepository;
import com.smartshop.repositories.SupermarketRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/shoplists")
public class ShoplistResource {

    @Autowired
    private ShoplistRepository shoplistRepository;

    @Autowired
    private SupermarketRepository supermarketRepository;

    @Autowired
    private ShoplistMapper shoplistMapper;

    @GetMapping
    public ResponseEntity<Set<ShoplistDto>> index() {

        Set<ShoplistDto> result = this.shoplistMapper
                .toDtoList(this.shoplistRepository.findAll()
                        .stream()
                        .collect(Collectors.toSet())
                );

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<ShoplistDto> store(@RequestBody ShoplistDto shoplist) {
        Shoplist list = this.shoplistRepository
                .save(this.shoplistMapper.fromDto(shoplist));

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(this.shoplistMapper.toDto(list));
    }

    @GetMapping("/{shoplist}")
    public ResponseEntity<ShoplistDto> show(@PathVariable("shoplist") Long id) {
        Optional<Shoplist> shoplist = this.shoplistRepository.findById(id);
        if(shoplist.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(this.shoplistMapper.toDto(shoplist.get()));

    }

    @DeleteMapping("/{shoplist}")
    public ResponseEntity<?> destroy(@PathVariable("shoplist") Long id) {
        Optional<Shoplist> shoplist = this.shoplistRepository.findById(id);
        if(shoplist.isEmpty()) return ResponseEntity.notFound().build();

        this.shoplistRepository.delete(shoplist.get());

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{shoplist}/best")
    public ResponseEntity getBestSupermarket(@PathVariable("shoplist") Long id) {
        String[] queryResults = this.shoplistRepository.getBestSupermarket(id)[0].split(",");
        BestSupermarket supermarket = new BestSupermarket();

        Supermarket best = this.supermarketRepository.findById(Long.parseLong(queryResults[0])).get();

        supermarket.setId(best.getId());
        supermarket.setName(best.getName());
        supermarket.setTotal(Double.parseDouble(queryResults[1]));

        return ResponseEntity.ok(
               supermarket
        );
    }

}
