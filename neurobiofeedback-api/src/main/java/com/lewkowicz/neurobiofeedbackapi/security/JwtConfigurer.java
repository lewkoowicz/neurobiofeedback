package com.lewkowicz.neurobiofeedbackapi.security;

import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;

public record JwtConfigurer(JwtDecoder jwtDecoder, JwtAuthenticationConverter jwtAuthenticationConverter) {
}
