package com.smartshop.resources;

import com.smartshop.dto.ShoplistDto;
import com.smartshop.dtoMappers.ShoplistMapper;
import com.smartshop.models.*;
import com.smartshop.models.responses.SupermarketResponse;
import com.smartshop.repositories.ShoplistRepository;
import com.smartshop.repositories.SupermarketRepository;
import com.smartshop.repositories.UserRepository;
import com.smartshop.services.GeolocationService;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.util.GeometricShapeFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/shoplists")
public class ShoplistResource {

    private final Logger log = LoggerFactory.getLogger(ShoplistResource.class);

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

        // the route is protected
        User user = this.userRepository.findByEmail(principal.getName())
                .orElseThrow(EntityNotFoundException::new);

        Set<ShoplistDto> result = this.shoplistMapper.toDtoList(user.getShoplists());

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<ShoplistDto> store(@RequestBody ShoplistDto shoplist, Principal principal) {

        Shoplist list = this.shoplistRepository.save(this.shoplistMapper.fromDto(shoplist));

        Optional<User> user = this.userRepository.findByEmail(principal.getName());

        if(user.isEmpty()) {
            log.info("USER HAS NO TOKEN");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        list.getUsers().add(user.get());
        user.get().getShoplists().add(list);

        this.shoplistRepository.flush();

        return ResponseEntity.status(HttpStatus.CREATED).body(this.shoplistMapper.toDto(list));
    }

    @GetMapping("/{shoplist}")
    public ResponseEntity<ShoplistDto> show(@PathVariable("shoplist") Long id, Principal principal) {

        User user = this.userRepository.findByEmail(principal.getName())
                .orElseThrow(EntityNotFoundException::new);

        Optional<Shoplist> result = user.getShoplists().stream()
                .filter(shoplist -> shoplist.getId().equals(id))
                .findFirst();

        if (result.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(this.shoplistMapper.toDto(result.get()));

    }

    // Remove user from that list, not actually delete the list!
    @DeleteMapping("/{shoplist}")
    public ResponseEntity<?> destroy(@PathVariable("shoplist") Long id, Principal principal) {
        // Check if user has a list with that ID
        Optional<User> user = this.userRepository.findByEmail(principal.getName());

        if(user.isPresent()) {
            Optional<Shoplist> listToDelete = user.get().getShoplists().stream()
                                            .filter(shoplist -> shoplist.getId().equals(id))
                                            .findFirst();
            if(listToDelete.isPresent()) {
                user.get().getShoplists().remove(listToDelete.get());
                listToDelete.get().getUsers().remove(user.get());
                this.shoplistRepository.flush();
            } else {
                return ResponseEntity.notFound().build();
            }
        }

        return ResponseEntity.noContent().build();
    }


//    @PostMapping("/{shoplist}/supermarkets")
//    public ResponseEntity<?> getBestSupermarket(@PathVariable("shoplist") Long id,
//                                                @Valid @RequestBody Position userPosition,
//                                                Principal principal) {
//
//        User user = this.userRepository.findByEmail(principal.getName()).get();
//
//        Optional<Shoplist> userShoplists = user.getShoplists().stream()
//                .filter(shoplist -> shoplist.getId().equals(id))
//                .findFirst();
//
//        if (userShoplists.isEmpty()) return ResponseEntity.notFound().build();
//
//        // get only IDs
//        List<Long> supermarketList = this.supermarketRepository.findAll()
//                                        .stream()
//                                        .map(Supermarket::getId)
//                                        .collect(Collectors.toList());
//
//
//        List<SupermarketResponse> results = this.shoplistRepository.getBestSupermarket(userShoplists.get().getId(), supermarketList)
//                .stream()
//                .map(supermarket -> {
//                    double distance = this.geolocationService.calculateDistanceBetweenPoints(
//                            userPosition,
//                            new Position(supermarket.getLatitude(), supermarket.getLongitude()));
//
//                    return new SupermarketResponse(supermarket.getSupermarket_id(),
//                            supermarket.getLongitude(),
//                            supermarket.getLatitude(),
//                            supermarket.getName(),
//                            supermarket.getTotal(),
//                            distance);
//
//                })
//                .collect(Collectors.toList());
//
//
//        return ResponseEntity.ok(results);
//
//    }

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

        var orderedByPriceSupermarkets = this.shoplistRepository
                .getAllSupermarketsWithRangeOrderedByPrice(shoplist, geometry);

        return ResponseEntity.ok(orderedByPriceSupermarkets);
    }



    private Geometry createCircle(double latitude, double longitude, final double radius) {
        GeometricShapeFactory shapeFactory = new GeometricShapeFactory();
        shapeFactory.setNumPoints(1000);
        shapeFactory.setCentre(new Coordinate(latitude, longitude));
        shapeFactory.setSize(radius * 0.024);
        return shapeFactory.createCircle();
    }

}
