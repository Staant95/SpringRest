package com.smartshop.resources;

import com.smartshop.dto.ShoplistDto;
import com.smartshop.dtoMappers.ShoplistMapper;
import com.smartshop.models.*;
import com.smartshop.models.responses.MessageResponse;
import com.smartshop.repositories.ShoplistRepository;
import com.smartshop.repositories.SupermarketRepository;
import com.smartshop.services.GeolocationService;
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

    @Autowired
    private GeolocationService geolocationService;


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
        if (shoplist.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(this.shoplistMapper.toDto(shoplist.get()));

    }

    @DeleteMapping("/{shoplist}")
    public ResponseEntity<?> destroy(@PathVariable("shoplist") Long id) {
        Optional<Shoplist> shoplist = this.shoplistRepository.findById(id);
        if (shoplist.isEmpty()) return ResponseEntity.notFound().build();

        this.shoplistRepository.delete(shoplist.get());

        return ResponseEntity.noContent().build();
    }


    @GetMapping("/{shoplist}/best")
    public ResponseEntity getBestSupermarket(@PathVariable("shoplist") Long id,
                                             @RequestBody Optional<Position> userPosition) {

        // unit is KM, -> at max 30km away from user
        double defaultMaxRange = 30.0;

        // get only IDs
        List<Supermarket> supermarketList = this.supermarketRepository.findAll();
        List<Long> inRangeSupermarketsId;


        if (userPosition.isPresent()) {
            Position position = userPosition.get();

            inRangeSupermarketsId = this.geolocationService
                                    .filterSupermarketsByDistance(position, supermarketList)
                                    .stream()
                                    .map(supermarket -> supermarket.getId())
                                    .collect(Collectors.toList());

            if(inRangeSupermarketsId.size() == 0) {
                return ResponseEntity.ok(new MessageResponse("Could not find any supermarket in that range. Please try a bigger range"));
            }

            return ResponseEntity.ok(
                    this.shoplistRepository.getBestSupermarket(id, inRangeSupermarketsId)
            );
        }

        inRangeSupermarketsId = supermarketList
                                .stream()
                                .map(supermarket -> supermarket.getId())
                                .collect(Collectors.toList());

        return ResponseEntity.ok(
                this.shoplistRepository.getBestSupermarket(id, inRangeSupermarketsId)
        );

    }


}
