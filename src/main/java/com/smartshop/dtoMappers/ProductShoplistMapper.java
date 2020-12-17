package com.smartshop.dtoMappers;

import com.smartshop.dto.ProductShoplistDto;
import com.smartshop.models.ProductShoplist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductShoplistMapper {

    @Mapping(target = "id", expression = "java(product.getProduct().getId())")
    @Mapping(target = "name", expression = "java(product.getProduct().getName())")
    @Mapping(target = "quantity", expression = "java(product.getQuantity())")
    ProductShoplistDto toDto(ProductShoplist product);


    @Mapping(target = "id", expression = "java(list.getProduct().getId())")
    @Mapping(target = "name", expression = "java(list.getProduct().getName())")
    @Mapping(target = "quantity", expression = "java(list.getQuantity())")
    List<ProductShoplistDto> toDtoList(List<ProductShoplist> list);
}
