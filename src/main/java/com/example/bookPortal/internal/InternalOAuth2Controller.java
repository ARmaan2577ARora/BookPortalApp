package com.example.bookPortal.internal;

import com.example.bookPortal.service.OAuth2LoginHandoffService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@RestController
@RequestMapping("/internal/oauth2")
public class InternalOAuth2Controller {

    private final OAuth2LoginHandoffService handoffService;

    public InternalOAuth2Controller(OAuth2LoginHandoffService handoffService) {
        this.handoffService = handoffService;
    }

    @GetMapping("/session")
    public Map<String, Object> session(@RequestParam String token) {
        Map<String, Object> user = handoffService.consume(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "OAuth2 login token expired or invalid"));
        return Map.of("user", user);
    }
}
