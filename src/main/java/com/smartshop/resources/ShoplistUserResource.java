package com.smartshop.resources;


import com.smartshop.dto.UserDto;
import com.smartshop.dtoMappers.UserMapper;
import com.smartshop.models.Shoplist;
import com.smartshop.models.User;
import com.smartshop.repositories.ShoplistRepository;
import com.smartshop.repositories.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/shoplists/{shoplist}/users")
public class ShoplistUserResource {

    private final ShoplistRepository shoplistRepository;

    private final UserRepository userRepository;

    private final UserMapper userMapper;

    public ShoplistUserResource(ShoplistRepository shoplistRepository, UserRepository userRepository, UserMapper userMapper) {
        this.shoplistRepository = shoplistRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> index(@PathVariable("shoplist") Long id) {
        Optional<Shoplist> shoplist = this.shoplistRepository.findById(id);

        if(shoplist.isEmpty()) return ResponseEntity.notFound().build();

        List<UserDto> result = this.userMapper.toDtoList(
                new ArrayList<>(shoplist.get().getUsers())
        );

        return ResponseEntity.ok(result);
    }


    @PostMapping
    public ResponseEntity<List<UserDto>> store(
            @PathVariable("shoplist") Long id,
            Principal principal) {

        Optional<User> loggedUser = this.userRepository.findByEmail(principal.getName());
        Optional<Shoplist> shoplist = this.shoplistRepository.findById(id);

        if(shoplist.isEmpty() || loggedUser.isEmpty()) return ResponseEntity.notFound().build();

        // Check if the user is already in the list, if true return the list of user
        if(shoplist.get().getUsers().contains(loggedUser.get()))
            return ResponseEntity.noContent().build();

        shoplist.get().getUsers().add(loggedUser.get());

        this.shoplistRepository.flush();

        List<UserDto> membersOfList = this.userMapper.toDtoList(
                new ArrayList<>(shoplist.get().getUsers())
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(membersOfList);
    }

    @DeleteMapping
    public ResponseEntity<ResponseStatus> destroy(
            @PathVariable("shoplist") Long id,
            Principal principal) {

        // I am sure I have the user, otherwise the request would have been blocked by JWT_Filter
        User loggedUser = this.userRepository.findByEmail(principal.getName()).get();
        Optional<Shoplist> shoplist = this.shoplistRepository.findById(id);

        if (shoplist.isEmpty()) return ResponseEntity.notFound().build();

        shoplist.get().getUsers().remove(loggedUser);
        this.shoplistRepository.flush();

        if(shoplist.get().getUsers().size() == 0) this.shoplistRepository.deleteById(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
