package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Roles;
import com.example.bookPortal.projection.RoleProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "roles", excerptProjection = RoleProjection.class)
public interface RolesRepo extends JpaRepository<Roles, Integer> {

    Optional<Roles> findByRoleNameIgnoreCase(String roleName);

    List<Roles> findByRoleNameContainingIgnoreCase(String roleName);

    boolean existsByRoleNameIgnoreCase(String roleName);

    List<Roles> findByRoleIdIn(List<Integer> roleIds);
}