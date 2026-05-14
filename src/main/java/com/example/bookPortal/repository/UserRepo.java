package com.example.bookPortal.repository;

import com.example.bookPortal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Integer> {

    Optional<User> findByEmailIgnoreCase(String email);

    boolean existsByEmailIgnoreCase(String email);

    Optional<User> findByPhone(String phone);

    boolean existsByPhone(String phone);

    List<User> findByFullNameContainingIgnoreCase(String fullName);

    List<User> findByIsActiveTrue();

    List<User> findByIsActiveFalse();

    List<User> findByRole_RoleId(Integer roleId);

    List<User> findByRole_RoleNameIgnoreCase(String roleName);
}