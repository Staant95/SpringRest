package com.smartshop.resources;

import com.smartshop.dto.ShoplistDto;
import com.smartshop.dtoMappers.ShoplistMapper;
import com.smartshop.models.*;
import com.smartshop.repositories.ShoplistRepository;
import com.smartshop.repositories.UserRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.util.GeometricShapeFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;


@RestController
@RequestMapping("/shoplists")
public class ShoplistResource {

    private final ShoplistRepository shoplistRepository;

    private final ShoplistMapper shoplistMapper;

    private final UserRepository userRepository;

    @Autowired
    public ShoplistResource(ShoplistRepository shoplistRepository,
                            ShoplistMapper shoplistMapper,
                            UserRepository userRepository) {

        this.shoplistRepository = shoplistRepository;
        this.shoplistMapper = shoplistMapper;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<?> index(Principal principal) {

        User user = this.userRepository.findByEmail(principal.getName())
                .orElseThrow(EntityNotFoundException::new);

        Set<ShoplistDto> result = this.shoplistMapper.toDtoList(user.getShoplists());

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<ShoplistDto> store(@RequestBody ShoplistDto shoplist, Principal principal) {

        Shoplist list = this.shoplistRepository.save(this.shoplistMapper.fromDto(shoplist));

        User user = this.userRepository
                .findByEmail(principal.getName())
                .orElseThrow(EntityNotFoundException::new);

        list.getUsers().add(user);
        user.getShoplists().add(list);

        this.shoplistRepository.flush();

        return ResponseEntity.status(HttpStatus.CREATED).body(this.shoplistMapper.toDto(list));
    }

    @GetMapping("/{shoplist}")
    public ResponseEntity<ShoplistDto> show(@PathVariable("shoplist") Long id, Principal principal) {

        User user = this.userRepository
                .findByEmail(principal.getName())
                .orElseThrow(EntityNotFoundException::new);

        Shoplist result = user.getShoplists().stream()
                .filter(shoplist -> shoplist.getId().equals(id))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);

        return ResponseEntity.ok(this.shoplistMapper.toDto(result));

    }

    // The shoplist exists only where there is at least one user associated with it
    @DeleteMapping("/{shoplist}")
    public ResponseEntity<?> destroy(@PathVariable("shoplist") Long id, Principal principal) {
        
        User user = this.userRepository.findByEmail(principal.getName())
                    .orElseThrow(EntityNotFoundException::new);

        Shoplist listToDelete = user.getShoplists().stream()
                                .filter(shoplist -> shoplist.getId().equals(id))
                                .findFirst()
                                .orElseThrow(EntityNotFoundException::new);

        user.getShoplists().remove(listToDelete);
        listToDelete.getUsers().remove(user);
        this.shoplistRepository.flush();        

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/{shoplist}/supermarkets")
    public ResponseEntity<?> getBest(
            @PathVariable("shoplist") Long id,
            @RequestParam(value = "range", required = false) Double range,
            @Valid @RequestBody Position userPosition,
            Principal principal) {

        var supermarketWithIn = range == null ? 5.0 : range; // kms

        User user = this.userRepository
                .findByEmail(principal.getName())
                .orElseThrow(EntityNotFoundException::new);

        Shoplist shoplist = user.getShoplists().stream()
                .filter(list -> list.getId().equals(id))
                .findFirst()
                .orElseThrow(EntityNotFoundException::new);

        Geometry geometry = createCircle(
                userPosition.getLatitude(),
                userPosition.getLongitude(),
                supermarketWithIn);

        var supermarketsOrderedByPrice = this.shoplistRepository
                .getAllSupermarketsWithinRangeOrderedByPrice(shoplist, geometry);

        return ResponseEntity.ok(supermarketsOrderedByPrice);
    }



    private Geometry createCircle(double latitude, double longitude, final double radius) {
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
        shapeFactory.setNumPoints(1000);
        shapeFactory.setCentre(new Coordinate(latitude, longitude));
        shapeFactory.setSize(radius * 0.024);
        return shapeFactory.createCircle();
    }

}
