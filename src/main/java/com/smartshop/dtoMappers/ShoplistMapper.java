package com.smartshop.dtoMappers;

import com.smartshop.dto.ShoplistDto;
import com.smartshop.models.Shoplist;
import org.mapstruct.Mapper;

import java.util.Set;

@Mapper(componentModel = "spring")
public interface ShoplistMapper {

    ShoplistDto toDto(Shoplist shoplist);

    Set<ShoplistDto> toDtoList (Set<Shoplist> list);

    Shoplist fromDto(ShoplistDto shoplistDto);

}
