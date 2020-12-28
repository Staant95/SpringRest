package com.smartshop.models.responses;

import com.smartshop.dto.ProductShoplistDto;
import com.smartshop.models.NotificationActionEnum;
import com.smartshop.models.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.Instant;
import java.util.Date;

@NoArgsConstructor
@Setter
@Getter
public class Notification implements Serializable {

    private ProductShoplistDto product;

    private User by;

    private NotificationActionEnum action;

    private Date created_at;

    public Notification(ProductShoplistDto product, NotificationActionEnum action) {
        this.product = product;
        this.action = action;
        this.created_at = Date.from(Instant.now());
    }

    public Notification(ProductShoplistDto product, NotificationActionEnum action, User user) {
        this.product = product;
        this.action = action;
        this.created_at = Date.from(Instant.now());
        this.by = user;
    }
}
