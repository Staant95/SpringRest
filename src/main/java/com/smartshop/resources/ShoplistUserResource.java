package com.smartshop.resources;


import com.smartshop.dto.UserDto;
import com.smartshop.dtoMappers.UserMapper;
import com.smartshop.models.MessageResponse;
import com.smartshop.models.Shoplist;
import com.smartshop.models.User;
import com.smartshop.repositories.ShoplistRepository;
import com.smartshop.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/shoplists/{shoplist}/users")
public class ShoplistUserResource {

    @Autowired
    private ShoplistRepository shoplistRepository;

    @Autowired
    private UserRepository userRepository;

    private final Logger logger = LoggerFactory.getLogger(ShoplistUserResource.class);

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public ResponseEntity<List<UserDto>> index(@PathVariable("shoplist") Long id) {
        Optional<Shoplist> shoplist = this.shoplistRepository.findById(id);

        if(shoplist.isEmpty()) return ResponseEntity.notFound().build();

        List<UserDto> result = this.userMapper.toDtoList(
                shoplist.get().getUsers().stream().collect(Collectors.toList())
        );

        return ResponseEntity.ok(result);
    }


    @PostMapping("/subscribe")
    public ResponseEntity<List<UserDto>> store(
            @PathVariable("shoplist") Long id,
            Principal principal) {

        // I am sure I have the user, otherwise the request would have been blocked by JWT_Filter
        Optional<User> loggedUser = this.userRepository.findByEmail(principal.getName());
        Optional<Shoplist> shoplist = this.shoplistRepository.findById(id);

        if(shoplist.isEmpty() || loggedUser.isEmpty()) return ResponseEntity.notFound().build();

        // Check if the user is already in the list, if true return the list of user
        if(shoplist.get().getUsers().contains(loggedUser.get()))
            return ResponseEntity.ok(this.userMapper.toDtoList(new ArrayList<>(shoplist.get().getUsers())));

        shoplist.get().getUsers().add(loggedUser.get());

        this.shoplistRepository.flush();

        List<UserDto> membersOfList = this.userMapper.toDtoList(
                new ArrayList<>(shoplist.get().getUsers())
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(membersOfList);
    }

    @DeleteMapping("/unsubscribe")
    public ResponseEntity<ResponseStatus> destroy(
            @PathVariable("shoplist") Long id,
            Principal principal) {

        // I am sure I have the user, otherwise the request would have been blocked by JWT_Filter
        User loggedUser = this.userRepository.findByEmail(principal.getName()).get();
        Optional<Shoplist> shoplist = this.shoplistRepository.findById(id);

        if (shoplist.isEmpty()) return ResponseEntity.notFound().build();

        logger.info("CURRENT USER -> " +  principal.getName());

        shoplist.get().getUsers().remove(loggedUser);
        this.shoplistRepository.flush();

        return ResponseEntity.status(HttpStatus.OK).build();
    }

}
