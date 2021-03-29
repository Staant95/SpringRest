package com.smartshop.dtoMappers;

import com.smartshop.dto.ShoplistDto;
import com.smartshop.dto.UserDto;
import com.smartshop.models.Shoplist;
import com.smartshop.models.User;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Set;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface ShoplistMapper {

    ShoplistDto toDto(Shoplist shoplist);

    @Mapping(target = "products", ignore = true)
    Set<ShoplistDto> toDtoList (Set<Shoplist> list);

    Shoplist fromDto(ShoplistDto shoplistDto);

}
