package com.smartshop.resources;


import com.smartshop.dto.ProductSupermarketDto;
import com.smartshop.dto.UserDto;
import com.smartshop.dtoMappers.ProductSupermarketMapper;
import com.smartshop.dtoMappers.UserMapper;
import com.smartshop.models.*;
import com.smartshop.repositories.*;
import net.bytebuddy.implementation.bind.annotation.Super;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.persistence.PreRemove;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/test")

public class Test {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    @GetMapping
    public ResponseEntity postSupermarket() {

        List<UserDto> users = userMapper.toDtoList(this.userRepository.findAll());
        return ResponseEntity.ok(users);

    }





}
