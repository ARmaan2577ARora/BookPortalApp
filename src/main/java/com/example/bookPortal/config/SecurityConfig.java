package com.example.bookPortal.config;

import com.example.bookPortal.service.BookPortalUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    private final BookPortalUserDetailsService userDetailsService;

    public SecurityConfig(BookPortalUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            response.setStatus(200);
            response.setContentType("application/json");
            response.getWriter().write("{\"message\":\"Login successful\"}");
        };
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationSuccessHandler authenticationSuccessHandler) throws Exception {

        return http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())

                .userDetailsService(userDetailsService)

                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                .authorizeHttpRequests(auth -> auth

                        // Preflight request for React/browser
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Login/logout
                        .requestMatchers("/login", "/logout").permitAll()

                        // Spring Data REST root/profile
                        .requestMatchers(HttpMethod.GET, "/api", "/api/").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/profile/**").permitAll()

                        // Public read-only catalogue endpoints
                        .requestMatchers(HttpMethod.GET, "/api/books/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/authors/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/publishers/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/bookAuthors/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/stores/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/storeBooks/**").permitAll()

                        // Public customer registration
                        .requestMatchers(HttpMethod.POST, "/api/users").permitAll()

                        // Customer + Admin: checkout, address, my orders
                        .requestMatchers("/api/addresses/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/orders/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/orders/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/orderItems/**").hasAnyRole("CUSTOMER", "ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/orderItems/**").hasAnyRole("CUSTOMER", "ADMIN")

                        // Admin only: role and user management
                        .requestMatchers("/api/roles/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/users/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/users/**").hasRole("ADMIN")

                        // Admin only: catalogue modification
                        .requestMatchers(HttpMethod.POST, "/api/books/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/books/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/books/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/books/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/authors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/authors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/authors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/authors/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/publishers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/publishers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/publishers/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/publishers/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/stores/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/stores/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/stores/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/stores/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/storeBooks/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/storeBooks/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/storeBooks/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/storeBooks/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.POST, "/api/bookAuthors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/bookAuthors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/api/bookAuthors/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/bookAuthors/**").hasRole("ADMIN")

                        // Admin only: order modification/deletion
                        .requestMatchers(HttpMethod.PATCH, "/api/orders/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/orders/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/orders/**").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.PATCH, "/api/orderItems/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/orderItems/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/orderItems/**").hasRole("ADMIN")

                        // Everything else blocked unless logged in
                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginProcessingUrl("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(authenticationSuccessHandler)
                        .failureHandler((request, response, exception) -> {
                            response.setStatus(401);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\":\"Invalid email or password\"}");
                        })
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessHandler((request, response, authentication) -> {
                            response.setStatus(200);
                            response.setContentType("application/json");
                            response.getWriter().write("{\"message\":\"Logout successful\"}");
                        })
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )

                .build();
    }
}