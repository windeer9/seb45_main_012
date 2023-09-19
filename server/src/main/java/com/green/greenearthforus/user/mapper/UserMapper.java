package com.green.greenearthforus.user.mapper;

import com.green.greenearthforus.user.entity.User;
import com.green.greenearthforus.user.dto.UserPostDto;
import com.green.greenearthforus.user.dto.UserResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User userPostDtoToUser(UserPostDto userPostDto);

    UserResponseDto userToUserResponseDto(User user);

}
