package com.example.bookPortal.service;

import com.example.bookPortal.entity.Roles;
import com.example.bookPortal.entity.User;
import com.example.bookPortal.repository.RolesRepo;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RepositoryEventHandler(User.class)
public class UserRepositoryEventHandler {

    private final PasswordEncoder passwordEncoder;
    private final RolesRepo rolesRepo;

    public UserRepositoryEventHandler(PasswordEncoder passwordEncoder,
                                      RolesRepo rolesRepo) {
        this.passwordEncoder = passwordEncoder;
        this.rolesRepo = rolesRepo;
    }

    @HandleBeforeCreate
    public void handleBeforeCreate(User user) {
        encodePasswordIfNeeded(user);

        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }

        if (user.getIsActive() == null) {
            user.setIsActive(true);
        }

        /*
         * If public user is registering, force role as CUSTOMER.
         * This prevents someone from registering himself as ADMIN.
         */
        if (!isLoggedInAdmin()) {
            Roles customerRole = rolesRepo.findByRoleNameIgnoreCase("CUSTOMER")
                    .orElseThrow(() -> new RuntimeException("CUSTOMER role not found"));

            user.setRole(customerRole);
        } else {
            /*
             * If admin is creating user and role is not provided,
             * default role will be CUSTOMER.
             */
            if (user.getRole() == null) {
                Roles customerRole = rolesRepo.findByRoleNameIgnoreCase("CUSTOMER")
                        .orElseThrow(() -> new RuntimeException("CUSTOMER role not found"));

                user.setRole(customerRole);
            }
        }
    }

    @HandleBeforeSave
    public void handleBeforeSave(User user) {
        encodePasswordIfNeeded(user);
    }

    private void encodePasswordIfNeeded(User user) {
        String password = user.getPasswordHash();

        if (password == null || password.isBlank()) {
            return;
        }

        if (!password.startsWith("$2a$")
                && !password.startsWith("$2b$")
                && !password.startsWith("$2y$")) {
            user.setPasswordHash(passwordEncoder.encode(password));
        }
    }

    private boolean isLoggedInAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            return false;
        }

        if (!authentication.isAuthenticated()) {
            return false;
        }

        if (authentication instanceof AnonymousAuthenticationToken) {
            return false;
        }

        return authentication.getAuthorities()
                .stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }
}