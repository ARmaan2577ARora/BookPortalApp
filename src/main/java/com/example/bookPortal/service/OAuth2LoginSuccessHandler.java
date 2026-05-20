package com.example.bookPortal.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final OAuth2LoginHandoffService handoffService;

    @Value("${app.frontend.origin:http://localhost:8081}")
    private String frontendOrigin;

    public OAuth2LoginSuccessHandler(OAuth2LoginHandoffService handoffService) {
        this.handoffService = handoffService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User principal = (OAuth2User) authentication.getPrincipal();

        Map<String, Object> user = new LinkedHashMap<>();
        user.put("userId", principal.getAttribute("appUserId"));
        user.put("fullName", principal.getAttribute("appUserName"));
        user.put("email", principal.getAttribute("appUserEmail"));
        user.put("phone", "");
        user.put("isActive", true);
        user.put("roleName", principal.getAttribute("roleName"));

        String token = handoffService.create(user);

        String redirectUrl = UriComponentsBuilder
                .fromUriString(frontendOrigin)
                .path("/oauth2/callback")
                .queryParam("token", token)
                .build()
                .toUriString();

        response.sendRedirect(redirectUrl);
    }
}
