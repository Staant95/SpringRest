package com.smartshop.resources;

import com.smartshop.dto.ShoplistDto;
import com.smartshop.dtoMappers.ShoplistMapper;
import com.smartshop.models.*;
import com.smartshop.models.responses.MessageResponse;
import com.smartshop.models.responses.SupermarketResponse;
import com.smartshop.repositories.ShoplistRepository;
import com.smartshop.repositories.SupermarketRepository;
import com.smartshop.repositories.UserRepository;
import com.smartshop.services.GeolocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.nio.file.attribute.UserPrincipal;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/shoplists")
@Slf4j
public class ShoplistResource {

    private final ShoplistRepository shoplistRepository;

    private final SupermarketRepository supermarketRepository;

    private final ShoplistMapper shoplistMapper;

    private final GeolocationService geolocationService;

    private final UserRepository userRepository;

    public ShoplistResource(ShoplistRepository shoplistRepository,
                            SupermarketRepository supermarketRepository,
                            ShoplistMapper shoplistMapper,
                            GeolocationService geolocationService,
                            UserRepository userRepository) {

        this.shoplistRepository = shoplistRepository;
        this.supermarketRepository = supermarketRepository;
        this.shoplistMapper = shoplistMapper;
        this.geolocationService = geolocationService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<Set<ShoplistDto>> index(Principal principal) {

        // the route is protected
        User user = this.userRepository.findByEmail(principal.getName()).get();

        Set<ShoplistDto> result = this.shoplistMapper.toDtoList(user.getShoplists());

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<ShoplistDto> store(@RequestBody ShoplistDto shoplist, Principal principal) {

        Shoplist list = this.shoplistRepository.save(this.shoplistMapper.fromDto(shoplist));

        User user = this.userRepository.findByEmail(principal.getName()).get();

        list.getUsers().add(user);
        user.getShoplists().add(list);

        this.shoplistRepository.flush();

        return ResponseEntity.status(HttpStatus.CREATED).body(this.shoplistMapper.toDto(list));
    }

    @GetMapping("/{shoplist}")
    public ResponseEntity<ShoplistDto> show(@PathVariable("shoplist") Long id, Principal principal) {

        User user = this.userRepository.findByEmail(principal.getName()).get();

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

        user.ifPresentOrElse(
                u -> log.info("Resource: Shoplist, Method: Delete, from user with id:" + u.getId()),
                () -> log.info("Could not find any user")
        );

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


    @GetMapping("/{shoplist}/supermarkets")
    public ResponseEntity<?> getBestSupermarket(@PathVariable("shoplist") Long id,
                                                @Valid @RequestBody Position userPosition,
                                                Principal principal) {

        User user = this.userRepository.findByEmail(principal.getName()).get();

        Optional<Shoplist> userShoplists = user.getShoplists().stream()
                .filter(shoplist -> shoplist.getId().equals(id))
                .findFirst();

        if (userShoplists.isEmpty()) return ResponseEntity.notFound().build();

        // get only IDs
        List<Supermarket> supermarketList = this.supermarketRepository.findAll();

        List<Long> inRangeSupermarketsId = new ArrayList<>(this.geolocationService
                .filterSupermarketsByDistance(userPosition, supermarketList, userPosition.getMaxDistance()));

        if (inRangeSupermarketsId.size() == 0) {
            return ResponseEntity.ok(new MessageResponse("Could not find any supermarket in that range. Please try a bigger range"));
        }

        List<SupermarketResponse> results = this.shoplistRepository.getBestSupermarket(userShoplists.get().getId(), inRangeSupermarketsId)
                .stream()
                .map(supermarket -> {
                    double distance = this.geolocationService.calculateDistanceBetweenPoints(
                            userPosition,
                            new Position(supermarket.getLatitude(), supermarket.getLongitude()));

                    return new SupermarketResponse(supermarket.getSupermarket_id(),
                            supermarket.getLongitude(),
                            supermarket.getLatitude(),
                            supermarket.getName(),
                            supermarket.getTotal(),
                            distance);

                })
                .collect(Collectors.toList());


        return ResponseEntity.ok(results);

    }


}
