package com.smartshop.dtoMappers;

import com.smartshop.dto.ProductShoplistDto;
import com.smartshop.models.ProductShoplist;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductShoplistMapper {

    @Mapping(target = "id", expression = "java(list.getProduct().getId())")
    @Mapping(target = "name", expression = "java(list.getProduct().getName())")
    @Mapping(target = "quantity", expression = "java(list.getQuantity())")
    ProductShoplistDto toDto(ProductShoplist list);
}
