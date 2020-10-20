package com.smartshop.resources;

import com.smartshop.exceptionHandlers.NotFoundException;
import com.smartshop.models.Supermarket;
import com.smartshop.repositories.SupermarketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/supermarkets")
public class SupermarketResource {

    @Autowired
    private SupermarketRepository supermarketRepository;


    @GetMapping
    public ResponseEntity<List<Supermarket>> index() {

        return ResponseEntity.ok(this.supermarketRepository.findAll());

    }


    @GetMapping("/{id}")
    public ResponseEntity<Supermarket> show(@PathVariable("id") Long id) {
        Optional<Supermarket> searched = this.supermarketRepository.findById(id);

        if(searched.isEmpty()) return ResponseEntity.notFound().build();

        return ResponseEntity.ok(searched.get());
    }


    @PostMapping
    public ResponseEntity<Supermarket> store(@Valid @RequestBody Supermarket supermarket) {
        Supermarket savedSupermarket = this.supermarketRepository.save(supermarket);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedSupermarket);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity destroy(@PathVariable("id") Long id) {

        Optional<Supermarket> searched = this.supermarketRepository.findById(id);

        if(searched.isEmpty()) return ResponseEntity.notFound().build();

        this.supermarketRepository.delete(searched.get());

        return ResponseEntity.noContent().build();

    }

}
