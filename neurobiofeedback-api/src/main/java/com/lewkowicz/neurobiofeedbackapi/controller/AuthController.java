package com.lewkowicz.neurobiofeedbackapi.controller;

import com.lewkowicz.neurobiofeedbackapi.constants.AuthConstants;
import com.lewkowicz.neurobiofeedbackapi.dto.LoginCredentialsDto;
import com.lewkowicz.neurobiofeedbackapi.dto.PasswordChangeDto;
import com.lewkowicz.neurobiofeedbackapi.dto.ResponseDto;
import com.lewkowicz.neurobiofeedbackapi.dto.UserDto;
import com.lewkowicz.neurobiofeedbackapi.service.impl.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Locale;
import java.util.Map;

@RestController
@RequestMapping(path = "/api", produces = (MediaType.APPLICATION_JSON_VALUE))
@Validated
@AllArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService;
    private final MessageSource messageSource;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserDto userDto) {
        authService.createUser(userDto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ResponseDto(HttpStatus.CREATED.toString(), getMessage(AuthConstants.ACCOUNT_CREATED)));
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginCredentialsDto loginRequest) {
        Map<String, Object> response = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        SecurityContextHolder.clearContext();
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        return ResponseEntity.ok(new ResponseDto(HttpStatus.OK.toString(), getMessage(AuthConstants.LOGOUT_SUCCESS)));
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changeUserPassword(@Valid @RequestBody PasswordChangeDto passwordChangeDto) {
        authService.changeUserPassword(passwordChangeDto.getEmail(), passwordChangeDto.getOldPassword(), passwordChangeDto.getNewPassword());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(), getMessage(AuthConstants.PASSWORD_CHANGED)));
    }

    @PostMapping("/delete-account")
    public ResponseEntity<?> deleteUserAccount(@Valid @RequestBody LoginCredentialsDto credentials) {
        authService.deleteAccount(credentials.getEmail(), credentials.getPassword());
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new ResponseDto(HttpStatus.OK.toString(), getMessage(AuthConstants.ACCOUNT_DELETED)));
    }

    private String getMessage(String key) {
        Locale locale = LocaleContextHolder.getLocale();
        return messageSource.getMessage(key, null, locale);
    }

}
