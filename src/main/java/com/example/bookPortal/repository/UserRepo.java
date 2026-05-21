package com.example.bookPortal.repository;

import com.example.bookPortal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bookPortal.projection.UserProjection;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "users", excerptProjection = UserProjection.class)

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
    Optional<UserProjection> findUserProjectionByEmailIgnoreCase(String email);

    List<UserProjection> findUserProjectionByFullNameContainingIgnoreCase(String fullName);

    List<UserProjection> findUserProjectionByRole_RoleNameIgnoreCase(String roleName);

    List<UserProjection> findUserProjectionByIsActiveTrue();

    List<UserProjection> findUserProjectionByIsActiveFalse();
}