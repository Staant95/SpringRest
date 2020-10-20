package com.smartshop.resources;

import com.smartshop.dto.ShoplistDto;
import com.smartshop.dtoMappers.ShoplistMapper;
import com.smartshop.models.Shoplist;
import com.smartshop.models.User;
import com.smartshop.repositories.ShoplistRepository;
import com.smartshop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/users/{user}/shoplists")
public class UserShoplistResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoplistRepository shoplistRepository;

    @Autowired
    private ShoplistMapper shoplistMapper;

    @GetMapping
    public ResponseEntity<Set<ShoplistDto>> index(@PathVariable("user") Long userId) {
        Optional<User> searchedUser = this.userRepository.findById(userId);
        if(searchedUser.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(shoplistMapper.toDtoList(searchedUser.get().getShoplists()));

    }

    @PostMapping
    public ResponseEntity<ShoplistDto> store(
            @PathVariable("user") Long id,
            @RequestBody ShoplistDto shoplistDto) {

        Optional<User> searchedUser = this.userRepository.findById(id);

        if(searchedUser.isEmpty()) return ResponseEntity.notFound().build();

        Shoplist shoplist = this.shoplistRepository.saveAndFlush(
                this.shoplistMapper.fromDto(shoplistDto)
        );
        searchedUser.get().getShoplists().add(shoplist);
        shoplist.getUsers().add(searchedUser.get());
        this.shoplistRepository.flush();

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.shoplistMapper.toDto(shoplist));
    }

}
