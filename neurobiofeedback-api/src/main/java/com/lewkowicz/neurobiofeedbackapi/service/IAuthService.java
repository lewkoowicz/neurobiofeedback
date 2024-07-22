package com.lewkowicz.neurobiofeedbackapi.service;

import com.lewkowicz.neurobiofeedbackapi.dto.LoginCredentialsDto;
import com.lewkowicz.neurobiofeedbackapi.dto.UserDto;

import java.util.Map;

public interface IAuthService {

    void createUser(UserDto userDto);

    Map<String, Object> authenticateUser(LoginCredentialsDto loginRequest);

    void changeUserPassword(String email, String oldPassword, String newPassword);

    void deleteAccount(String email, String password);

}
