package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RolesRepo extends JpaRepository<Roles,Integer> {

    // Find role by exact role name, ignoring uppercase/lowercase
    Optional<Roles> findByRoleNameIgnoreCase(String roleName);

    // Search roles where role name contains a keyword
    List<Roles> findByRoleNameContainingIgnoreCase(String roleName);

    // Check if a role already exists
    boolean existsByRoleNameIgnoreCase(String roleName);

    // Search roles starting with given text
    List<Roles> findByRoleNameStartingWithIgnoreCase(String prefix);

    // Find multiple roles by role IDs
    List<Roles> findByRoleIdIn(List<Integer> roleIds);

}
