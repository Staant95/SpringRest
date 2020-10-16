package com.smartshop.resources;

import com.smartshop.models.Shoplist;
import com.smartshop.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/users/{user_id}/shoplists")
public class UserShoplistResource {

    @Autowired
    private UserRepository userRepository;

    @GetMapping
    public Long index(@PathVariable("user_id") Long userId) {

        return userId;
    }

}
