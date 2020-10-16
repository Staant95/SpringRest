package com.smartshop.repositories;

import com.smartshop.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRespository extends JpaRepository<Token, Long> {
}
