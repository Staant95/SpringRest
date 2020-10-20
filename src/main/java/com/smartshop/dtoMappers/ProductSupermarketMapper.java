package com.smartshop.dtoMappers;


import com.smartshop.dto.ProductSupermarketDto;
import com.smartshop.models.Product;
import com.smartshop.models.ProductSupermarket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductSupermarketMapper {

    @Mapping(target = "id", expression = "java(ps.getProduct().getId())")
    @Mapping(target = "name", expression = "java(ps.getProduct().getName())")
    @Mapping(target = "price", expression = "java(ps.getPrice())")
    ProductSupermarketDto toDto(ProductSupermarket ps);

}
