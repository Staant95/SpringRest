package com.smartshop.dtoMappers;


import com.smartshop.dto.UserDto;
import com.smartshop.models.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = ShoplistMapper.class)
public interface UserMapper {

    UserDto toDto(User user);

    List<UserDto> toDtoList(List<User> users);

    User fromDto(UserDto user);
}
