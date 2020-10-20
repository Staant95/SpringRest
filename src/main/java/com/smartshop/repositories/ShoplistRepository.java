package com.smartshop.repositories;

import com.smartshop.models.BestSupermarket;
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


    @Transactional
    @Query(value = "select * from (select supermarket_id,sum(price) as total " +
            "from product_supermarket where product_id IN " +
            "(select product_id from product_shoplist where shoplist_id =:shoplist) group by supermarket_id) " +
            "as p order by p.total limit 1;", nativeQuery = true)
    String[] getBestSupermarket(@Param("shoplist") Long shoplist);

}
