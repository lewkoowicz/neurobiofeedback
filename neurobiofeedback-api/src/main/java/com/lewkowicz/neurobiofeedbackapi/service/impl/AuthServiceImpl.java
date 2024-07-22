package com.lewkowicz.neurobiofeedbackapi.service.impl;

import com.lewkowicz.neurobiofeedbackapi.constants.AuthConstants;
import com.lewkowicz.neurobiofeedbackapi.dto.LoginCredentialsDto;
import com.lewkowicz.neurobiofeedbackapi.dto.UserDto;
import com.lewkowicz.neurobiofeedbackapi.entity.User;
import com.lewkowicz.neurobiofeedbackapi.exception.AccountAlreadyExistsException;
import com.lewkowicz.neurobiofeedbackapi.exception.LoginFailedException;
import com.lewkowicz.neurobiofeedbackapi.exception.PasswordsDoNotMatchException;
import com.lewkowicz.neurobiofeedbackapi.exception.ResourceNotFoundException;
import com.lewkowicz.neurobiofeedbackapi.repository.UserRepository;
import com.lewkowicz.neurobiofeedbackapi.security.TokenService;
import com.lewkowicz.neurobiofeedbackapi.security.UserDetailsServiceImpl;
import com.lewkowicz.neurobiofeedbackapi.service.IAuthService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements IAuthService {

    private final UserRepository userRepository;
    private final UserDetailsServiceImpl userDetailsService;
    private final TokenService tokenService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createUser(UserDto userDto) {
        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
        if (optionalUser.isPresent()) {
            throw new AccountAlreadyExistsException(AuthConstants.ACCOUNT_ALREADY_EXISTS);
        }
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setPassword(userDto.getPassword());
        user.setRole("USER");
        userDetailsService.saveUser(user);
    }

    @Override
    public Map<String, Object> authenticateUser(LoginCredentialsDto loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getEmail(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenService.generateToken(authentication);
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String role = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .findFirst()
                    .orElseThrow(() -> new ResourceNotFoundException(AuthConstants.ROLE_NOT_FOUND));

            Map<String, Object> response = new HashMap<>();
            response.put("token", jwt);
            response.put("role", role);
            response.put("email", loginRequest.getEmail());
            return response;
        } catch (Exception e) {
            throw new LoginFailedException(AuthConstants.LOGIN_FAILED);
        }
    }

    @Override
    public void changeUserPassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new PasswordsDoNotMatchException(AuthConstants.WRONG_PASSWORD);
        }

        if (passwordEncoder.matches(newPassword, user.getPassword())) {
            throw new PasswordsDoNotMatchException(AuthConstants.NEW_PASSWORD_CAN_NOT_BE_THE_SAME);
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void deleteAccount(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new PasswordsDoNotMatchException(AuthConstants.WRONG_PASSWORD);
        }

        userRepository.delete(user);
    }

}
