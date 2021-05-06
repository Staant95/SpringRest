package com.smartshop.models.requestBody;

import javax.validation.constraints.NotNull;


public class ProductInSupermarket {

    @NotNull(message = "productId is required")
    private Long productId;

    @NotNull(message = "Price is required")
    private Double price;

    public ProductInSupermarket() {
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
