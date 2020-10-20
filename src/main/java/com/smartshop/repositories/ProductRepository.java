package com.smartshop.repositories;

import com.smartshop.models.Product;
import com.smartshop.models.Supermarket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
