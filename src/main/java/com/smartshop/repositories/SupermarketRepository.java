package com.smartshop.repositories;

import com.smartshop.models.Supermarket;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SupermarketRepository extends JpaRepository<Supermarket, Long> {

//    @Query("select s from Supermarket s where within(s.location, :range) = true")
//    List<Supermarket> getAllWithin(@Param("range") Geometry range);
}
