package com.example.bookPortal.service;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class OAuth2LoginHandoffService {

    private final Map<String, LoginPayload> handoffs = new ConcurrentHashMap<>();

    public String create(Map<String, Object> user) {
        String token = UUID.randomUUID().toString();
        handoffs.put(token, new LoginPayload(user, Instant.now().plusSeconds(120)));
        return token;
    }

    public Optional<Map<String, Object>> consume(String token) {
        if (token == null || token.isBlank()) {
            return Optional.empty();
        }

        LoginPayload payload = handoffs.remove(token);
        if (payload == null || payload.expiresAt().isBefore(Instant.now())) {
            return Optional.empty();
        }

        return Optional.of(new LinkedHashMap<>(payload.user()));
    }

    private record LoginPayload(Map<String, Object> user, Instant expiresAt) {}
}
