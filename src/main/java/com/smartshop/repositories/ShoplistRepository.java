package com.smartshop.repositories;

import com.smartshop.dto.SupermarketByPriceDTO;
import com.smartshop.models.Shoplist;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ShoplistRepository extends JpaRepository<Shoplist, Long> {

    @Query(value =
            "select new com.smartshop.dto.SupermarketByPriceDTO(SUM(ps.price) as total, ps.supermarket as supermarket) " +
            "from ProductSupermarket ps " +
            "where ps.product in (select p.product from ProductShoplist p where p.shoplist = :shoplist) " +
            "and ps.supermarket in (select s from Supermarket s where within(s.location, :range) = true) " +
            "group by ps.supermarket order by total ASC"
    )
    List<SupermarketByPriceDTO> getAllSupermarketsWithinRangeOrderedByPrice(@Param("shoplist") Shoplist shoplist, @Param("range") Geometry range);

}
