package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolesRepo extends JpaRepository<Roles, Integer> {

    Optional<Roles> findByRoleNameIgnoreCase(String roleName);

    List<Roles> findByRoleNameContainingIgnoreCase(String roleName);

    boolean existsByRoleNameIgnoreCase(String roleName);

    List<Roles> findByRoleIdIn(List<Integer> roleIds);
}