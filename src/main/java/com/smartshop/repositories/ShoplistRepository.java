package com.smartshop.repositories;

import com.smartshop.models.BestSupermarket;
import com.smartshop.models.Shoplist;
import com.smartshop.models.TestSup;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ShoplistRepository extends JpaRepository<Shoplist, Long> {


    @Transactional
    @Query(
            value = "select sup.name, sup.latitude, sup.longitude, supermarket_id, SUM(price) as total from product_supermarket " +
                    "join supermarket as sup on sup.id = supermarket_id " +
                    "where product_id IN (select product_id from product_shoplist where shoplist_id =:shoplist) " +
                    "AND supermarket_id IN (:supermarkets)" +
                    " group by supermarket_id order by total ASC",
            nativeQuery = true
    )
    //BestSupermarket must be an interface
    List<BestSupermarket> getBestSupermarket(@Param("shoplist") Long shoplist, @Param("supermarkets")List<Long> supermarkets);


    // select SUM(ps.price) as total from product_supermarket as ps
    // where
    // product_id IN (select product_id from product_shoplist where shoplist_id = VAR)
    // AND
    // supermarket_id IN (1,2)
    // group by ps.supermarket_id order by total ASC;

    // "where within(s.location, :range) = true"
    @Query(value =
            "select SUM(ps.price) as total, ps.supermarket as supermarket " +
            "from ProductSupermarket ps " +
            "where ps.product in (select p.product from ProductShoplist p where p.shoplist = :shoplist) " +
            "and ps.supermarket in (select s from Supermarket s where within(s.location, :range) = true) " +
            "group by ps.supermarket order by total ASC"
    )
    List<TestSup> getAllSupermarketsWithRangeOrderedByPrice(@Param("shoplist") Shoplist shoplist, @Param("range") Geometry range);

}
