package com.smartshop.dtoMappers;

import com.smartshop.dto.ShoplistDto;
import com.smartshop.models.Shoplist;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Set;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ShoplistMapper {

    ShoplistDto toDto(Shoplist shoplist);

    @Mapping(target = "products", ignore = true)
    Set<ShoplistDto> toDtoList (Set<Shoplist> list);

    @Mapping(target = "users", ignore = true)
    Shoplist fromDto(ShoplistDto shoplistDto);

}
