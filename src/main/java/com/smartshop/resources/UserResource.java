package com.smartshop.resources;

import com.smartshop.models.User;
import com.smartshop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserResource {

    @Autowired
    private UserRepository userRepository;


    @GetMapping(produces = "application/json")
    public List<User> index() {
        return userRepository.findAll();

    }


    @GetMapping
    @RequestMapping("/{user_id}")
    public User show(@PathVariable("user_id") Long id) {
        if(this.userRepository.findByEmail("stas@gmail.com").isPresent())
            return this.userRepository.findByEmail("stas@gmail.com").get();

        return new User();
    }
}
