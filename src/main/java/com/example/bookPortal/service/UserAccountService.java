package com.example.bookPortal.service;

import com.example.bookPortal.entity.Roles;
import com.example.bookPortal.entity.User;
import com.example.bookPortal.repository.RolesRepo;
import com.example.bookPortal.repository.UserRepo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class UserAccountService {

    private final UserRepo userRepo;
    private final RolesRepo rolesRepo;
    private final PasswordEncoder passwordEncoder;

    public UserAccountService(UserRepo userRepo,
                              RolesRepo rolesRepo,
                              PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.rolesRepo = rolesRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User createCustomer(String fullName, String email, String rawPassword, String phone) {
        return createUser(fullName, email, rawPassword, phone, "CUSTOMER");
    }

    @Transactional
    public User createAdmin(String fullName, String email, String rawPassword, String phone) {
        return createUser(fullName, email, rawPassword, phone, "ADMIN");
    }

    private User createUser(String fullName,
                            String email,
                            String rawPassword,
                            String phone,
                            String roleName) {
        if (userRepo.existsByEmailIgnoreCase(email)) {
            throw new RuntimeException("Email already exists: " + email);
        }

        Roles role = rolesRepo.findByRoleNameIgnoreCase(roleName)
                .orElseThrow(() -> new RuntimeException(roleName + " role not found"));

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email.toLowerCase());
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setPhone(phone);
        user.setRole(role);
        user.setIsActive(true);
        user.setCreatedAt(LocalDateTime.now());

        return userRepo.save(user);
    }
}