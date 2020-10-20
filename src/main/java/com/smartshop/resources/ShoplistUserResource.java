package com.smartshop.resources;


import com.smartshop.dto.ShoplistDto;
import com.smartshop.dto.UserDto;
import com.smartshop.dtoMappers.UserMapper;
import com.smartshop.models.Shoplist;
import com.smartshop.models.User;
import com.smartshop.models.requestBody.UserId;
import com.smartshop.repositories.ShoplistRepository;
import com.smartshop.repositories.UserRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    // attach user to shoplist
    @PostMapping
    public ResponseEntity<List<UserDto>> store(
            @PathVariable("shoplist") Long id,
            @RequestBody UserId user) {

        Optional<Shoplist> shoplist = this.shoplistRepository.findById(id);
        Optional<User> u = this.userRepository.findById(user.getUserId());

        if( !(shoplist.isPresent() && u.isPresent()) ) return ResponseEntity.notFound().build();

        shoplist.get().getUsers().add(u.get());
        this.shoplistRepository.flush();

        List<UserDto> membersOfList = this.userMapper.toDtoList(
                shoplist.get().getUsers().stream().collect(Collectors.toList())
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(membersOfList);
    }



}
