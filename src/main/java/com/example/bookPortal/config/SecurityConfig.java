package com.example.bookPortal.config;

import com.example.bookPortal.service.BookPortalOAuth2UserService;
import com.example.bookPortal.service.BookPortalUserDetailsService;
import com.example.bookPortal.service.OAuth2LoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class SecurityConfig {

    private final BookPortalUserDetailsService userDetailsService;
    private final BookPortalOAuth2UserService oauth2UserService;
    private final OAuth2LoginSuccessHandler oauth2LoginSuccessHandler;

    public SecurityConfig(BookPortalUserDetailsService userDetailsService,
                          BookPortalOAuth2UserService oauth2UserService,
                          OAuth2LoginSuccessHandler oauth2LoginSuccessHandler) {
        this.userDetailsService = userDetailsService;
        this.oauth2UserService = oauth2UserService;
        this.oauth2LoginSuccessHandler = oauth2LoginSuccessHandler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .userDetailsService(userDetailsService)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // OAuth2 login endpoints for GitHub.
                        .requestMatchers("/oauth2/**", "/login/oauth2/**").permitAll()

                        // Internal endpoints are used by the separate Thymeleaf frontend app on port 8081.
                        .requestMatchers("/internal/**").permitAll()

                        // Spring Data REST read APIs are public for catalogue pages.
                        .requestMatchers(HttpMethod.GET, "/api", "/api/", "/api/profile/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/books/**", "/api/authors/**", "/api/publishers/**", "/api/bookAuthors/**", "/api/stores/**", "/api/storeBooks/**").permitAll()

                        // Write APIs remain protected for direct backend/Postman access.
                        .requestMatchers("/api/addresses/**", "/api/orders/**", "/api/orderItems/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers("/api/roles/**", "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/books/**", "/api/authors/**", "/api/publishers/**", "/api/stores/**", "/api/storeBooks/**", "/api/bookAuthors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/books/**", "/api/authors/**", "/api/publishers/**", "/api/stores/**", "/api/storeBooks/**", "/api/bookAuthors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/books/**", "/api/authors/**", "/api/publishers/**", "/api/stores/**", "/api/storeBooks/**", "/api/bookAuthors/**", "/api/orders/**", "/api/orderItems/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**", "/api/authors/**", "/api/publishers/**", "/api/stores/**", "/api/storeBooks/**", "/api/bookAuthors/**", "/api/orders/**", "/api/orderItems/**").hasRole("ADMIN")
                        .anyRequest().authenticated()
                )
                .oauth2Login(oauth2 -> oauth2
                        .userInfoEndpoint(userInfo -> userInfo.userService(oauth2UserService))
                        .successHandler(oauth2LoginSuccessHandler)
                )
                .httpBasic(Customizer.withDefaults())
                .build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:8081", "http://127.0.0.1:8081"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
