package com.lewkowicz.neurobiofeedbackapi.mapper;

import com.lewkowicz.neurobiofeedbackapi.dto.UserDto;
import com.lewkowicz.neurobiofeedbackapi.entity.User;

public class UserMapper {

    public static UserDto mapToUserDto(User user, UserDto userDto) {
        userDto.setUserId(user.getUserId());
        userDto.setEmail(user.getEmail());
        userDto.setPassword(user.getPassword());
        userDto.setRole(user.getRole());
        return userDto;
    }

    public static User mapToUser(UserDto userDto, User user) {
        user.setUserId(userDto.getUserId());
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole(userDto.getRole());
        return user;
    }

}
