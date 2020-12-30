package com.smartshop.models.requestBody;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;


@Setter
@Getter
@NoArgsConstructor
public class ProductInSupermarket {

    @NotNull(message = "productId is required")
    private Long productId;

    @NotNull(message = "Price is required")
    private Double price;

}
