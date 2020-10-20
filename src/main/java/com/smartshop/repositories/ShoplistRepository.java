package com.smartshop.repositories;

import com.smartshop.models.Product;
import com.smartshop.models.Shoplist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;

public interface ShoplistRepository extends JpaRepository<Shoplist, Long> {

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = "update product_shoplist ps set ps.quantity =:quantity where ps.product_id =:product and ps.shoplist_id = :shoplist",
    nativeQuery = true)
    void updateQuantity(@Param("product") Long product, @Param("shoplist") Long shoplist, @Param("quantity") int quantity);
}
