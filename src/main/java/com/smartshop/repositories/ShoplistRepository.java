package com.smartshop.repositories;

import com.smartshop.models.BestSupermarket;
import com.smartshop.models.Shoplist;
import com.smartshop.models.Supermarket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface ShoplistRepository extends JpaRepository<Shoplist, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update product_shoplist ps set ps.quantity =:quantity where ps.product_id =:product and ps.shoplist_id = :shoplist",
    nativeQuery = true)
    void updateQuantity(@Param("product") Long product, @Param("shoplist") Long shoplist, @Param("quantity") int quantity);


    @Transactional
    @Query(
            value = "select supermarket_id, SUM(price) as total from product_supermarket where product_id IN (" +
                    "select product_id from product_shoplist where shoplist_id =:shoplist) AND supermarket_id IN (:supermarkets)" +
                    " group by supermarket_id order by total ASC",
            nativeQuery = true
    )
    //BestSupermarket must be an interface
    List<BestSupermarket> getBestSupermarket(@Param("shoplist") Long shoplist, @Param("supermarkets")List<Long> supermarkets);


    @Transactional
    @Query(
            value = "select count(*) from product_shoplist where shoplist_id =:shoplistId and product_id =:productId",
            nativeQuery = true
    )
    Integer containsProduct(@Param("shoplistId") Long shoplistId, @Param("productId") Long productId);

    @Transactional
    @Query(
            value = "select quantity from product_shoplist where shoplist_id =:shoplistId and product_id =:productId",
            nativeQuery = true
    )
    Integer getProductQuantity(@Param("shoplistId") Long shoplistId, @Param("productId") Long productId);

}
