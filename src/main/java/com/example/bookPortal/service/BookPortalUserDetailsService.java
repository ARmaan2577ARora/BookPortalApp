package com.example.bookPortal.service;

import com.example.bookPortal.entity.User;
import com.example.bookPortal.repository.UserRepo;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookPortalUserDetailsService implements UserDetailsService {

    private final UserRepo userRepo;

    public BookPortalUserDetailsService(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        String cleanEmail = email.trim();

        System.out.println("LOGIN EMAIL RECEIVED: " + cleanEmail);

        User user = userRepo.findByEmailIgnoreCase(cleanEmail)
                .orElseThrow(() -> {
                    System.out.println("USER NOT FOUND: " + cleanEmail);
                    return new UsernameNotFoundException("User not found with email: " + cleanEmail);
                });

        System.out.println("USER FOUND: " + user.getEmail());
        System.out.println("HASH FROM DB: " + user.getPasswordHash());
        System.out.println("ACTIVE: " + user.getIsActive());

        if (user.getRole() == null || user.getRole().getRoleName() == null) {
            System.out.println("ROLE MISSING");
            throw new UsernameNotFoundException("User role is missing");
        }

        String roleName = user.getRole().getRoleName().toUpperCase();

        System.out.println("ROLE: " + roleName);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPasswordHash())
                .authorities(List.of(new SimpleGrantedAuthority("ROLE_" + roleName)))
                .disabled(Boolean.FALSE.equals(user.getIsActive()))
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .build();
    }
}