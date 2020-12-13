package com.smartshop.models.requestBody;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Setter
@Getter
@NoArgsConstructor
public class ProductAndPrice {


    private Long productId;


    private double price;

}
