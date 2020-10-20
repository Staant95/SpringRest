package com.smartshop.dtoMappers;

import com.smartshop.dto.ProductDto;
import com.smartshop.models.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductDto toDto(Product product);

    List<ProductDto> toDtoList(List<Product> products);

    Product fromDto(ProductDto productDto);

}

