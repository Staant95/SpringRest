package com.smartshop.repositories;

import com.smartshop.models.Token;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRespository extends JpaRepository<Token, Long> {
    Optional<Token> findByToken(String token);
}
