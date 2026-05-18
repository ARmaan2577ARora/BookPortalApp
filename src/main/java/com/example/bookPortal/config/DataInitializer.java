package com.example.bookPortal.config;

import com.example.bookPortal.entity.Roles;
import com.example.bookPortal.entity.User;
import com.example.bookPortal.repository.RolesRepo;
import com.example.bookPortal.repository.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RolesRepo rolesRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(RolesRepo rolesRepo,
                           UserRepo userRepo,
                           PasswordEncoder passwordEncoder) {
        this.rolesRepo = rolesRepo;
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        Roles adminRole = getOrCreateRole("ADMIN");
        Roles customerRole = getOrCreateRole("CUSTOMER");

        createOrUpdateUser("Admin User", "admin@bookportal.com", "1234", "9876543210", adminRole);
        createOrUpdateUser("Rahul Sharma", "rahul@gmail.com", "1234", "9876501234", customerRole);
        createOrUpdateUser("Priya Singh", "priya@gmail.com", "1234", "9876505678", customerRole);
        createOrUpdateUser("Aman Verma", "aman@gmail.com", "1234", "9876511111", customerRole);
    }

    private Roles getOrCreateRole(String roleName) {
        return rolesRepo.findByRoleNameIgnoreCase(roleName)
                .orElseGet(() -> {
                    Roles role = new Roles();
                    role.setRoleName(roleName);
                    return rolesRepo.save(role);
                });
    }

    private void createOrUpdateUser(String fullName,
                                    String email,
                                    String rawPassword,
                                    String phone,
                                    Roles role) {

        User user = userRepo.findByEmailIgnoreCase(email)
                .orElseGet(User::new);

        user.setFullName(fullName);
        user.setEmail(email);
        user.setPasswordHash(passwordEncoder.encode(rawPassword));
        user.setPhone(phone);
        user.setRole(role);
        user.setIsActive(true);

        if (user.getCreatedAt() == null) {
            user.setCreatedAt(LocalDateTime.now());
        }

        userRepo.save(user);
    }
}