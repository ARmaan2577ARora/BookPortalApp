package com.example.bookPortal.service;

import com.example.bookPortal.entity.User;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class BookPortalOAuth2UserService extends DefaultOAuth2UserService {

    private final UserAccountService userAccountService;
    private final RestTemplate restTemplate = new RestTemplate();

    public BookPortalOAuth2UserService(UserAccountService userAccountService) {
        this.userAccountService = userAccountService;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oauth2User = super.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        if (!"github".equalsIgnoreCase(registrationId)) {
            throw new OAuth2AuthenticationException("Only GitHub OAuth2 login is configured");
        }

        Map<String, Object> attributes = new LinkedHashMap<>(oauth2User.getAttributes());

        String githubId = string(attributes.get("id"));
        String login = string(attributes.get("login"));
        String name = string(attributes.get("name"));
        String email = string(attributes.get("email"));

        if (name.isBlank()) {
            name = login.isBlank() ? "GitHub User" : login;
        }

        if (email.isBlank()) {
            email = fetchPrimaryGithubEmail(userRequest.getAccessToken().getTokenValue());
        }

        if (email.isBlank()) {
            // GitHub can hide private emails. This fallback keeps the project login usable.
            email = (login.isBlank() ? "github-user-" + githubId : login) + "@github.local";
        }

        User user = userAccountService.findOrCreateOAuth2Customer(name, email, "GITHUB", githubId);
        String roleName = user.getRole() == null ? "CUSTOMER" : user.getRole().getRoleName();

        attributes.put("appUserId", user.getUserId());
        attributes.put("appUserName", user.getFullName());
        attributes.put("appUserEmail", user.getEmail());
        attributes.put("roleName", roleName);

        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + roleName.toUpperCase()));

        return new DefaultOAuth2User(authorities, attributes, "login");
    }

    private String fetchPrimaryGithubEmail(String accessToken) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(accessToken);
            headers.setAccept(List.of(MediaType.APPLICATION_JSON));
            headers.set("X-GitHub-Api-Version", "2022-11-28");

            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    "https://api.github.com/user/emails",
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    new ParameterizedTypeReference<>() {}
            );

            List<Map<String, Object>> emails = response.getBody();
            if (emails == null) {
                return "";
            }

            for (Map<String, Object> item : emails) {
                if (bool(item.get("primary")) && bool(item.get("verified"))) {
                    return string(item.get("email"));
                }
            }

            for (Map<String, Object> item : emails) {
                if (bool(item.get("verified"))) {
                    return string(item.get("email"));
                }
            }
        } catch (Exception ignored) {
            return "";
        }
        return "";
    }

    private String string(Object value) {
        return value == null ? "" : String.valueOf(value).trim();
    }

    private boolean bool(Object value) {
        return Boolean.TRUE.equals(value) || "true".equalsIgnoreCase(String.valueOf(value));
    }
}
