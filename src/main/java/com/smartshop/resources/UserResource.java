package com.smartshop.resources;

import com.smartshop.dto.UserDto;
import com.smartshop.dtoMappers.UserMapper;
import com.smartshop.models.User;
import com.smartshop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserResource {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<UserDto>> index() {

        return ResponseEntity.ok(userMapper.toDtoList(userRepository.findAll()));

    }


    @GetMapping
    @RequestMapping("/{user}")
    public ResponseEntity<UserDto> show(@PathVariable("user") Long id) {

        Optional<User> searchedUser = this.userRepository.findById(id);

        if(searchedUser.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(this.userMapper.toDto(searchedUser.get()));
    }
}
