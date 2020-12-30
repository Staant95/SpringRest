package com.smartshop.models.requestBody;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;


@Setter
@Getter
@NoArgsConstructor
public class ProductInSupermarket {

    @NotBlank(message = "Price is required")
    private Long productId;

    @NotBlank(message = "Price is required")
    private double price;

}
