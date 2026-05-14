package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RolesRepoTest {

    @Autowired
    private RolesRepo rolesRepo;

    private Roles adminRole;
    private Roles customerRole;

    private Roles createRole(String roleName) {
        Roles role = new Roles();
        role.setRoleName(roleName);
        return role;
    }

    private Roles getOrCreateRole(String roleName) {
        return rolesRepo.findByRoleNameIgnoreCase(roleName)
                .orElseGet(() -> rolesRepo.save(createRole(roleName)));
    }

    @BeforeEach
    void setUp() {
        adminRole = getOrCreateRole("ADMIN");
        customerRole = getOrCreateRole("CUSTOMER");
    }

    @Test
    void findByRoleNameIgnoreCase_WhenAdminExists_ReturnsAdminRole() {
        Optional<Roles> result = rolesRepo.findByRoleNameIgnoreCase("admin");

        assertThat(result).isPresent();
        assertThat(result.get().getRoleName()).isEqualToIgnoringCase("ADMIN");
    }

    @Test
    void findByRoleNameIgnoreCase_WhenCustomerExists_ReturnsCustomerRole() {
        Optional<Roles> result = rolesRepo.findByRoleNameIgnoreCase("customer");

        assertThat(result).isPresent();
        assertThat(result.get().getRoleName()).isEqualToIgnoringCase("CUSTOMER");
    }

    @Test
    void findByRoleNameIgnoreCase_WhenRoleDoesNotExist_ReturnsEmptyOptional() {
        Optional<Roles> result = rolesRepo.findByRoleNameIgnoreCase("AUTHOR");

        assertThat(result).isEmpty();
    }

    @Test
    void findByRoleNameContainingIgnoreCase_WhenKeywordAdm_ReturnsAdmin() {
        List<Roles> result = rolesRepo.findByRoleNameContainingIgnoreCase("adm");

        assertThat(result)
                .extracting(Roles::getRoleName)
                .anyMatch(roleName -> roleName.equalsIgnoreCase("ADMIN"));
    }

    @Test
    void findByRoleNameContainingIgnoreCase_WhenKeywordCust_ReturnsCustomer() {
        List<Roles> result = rolesRepo.findByRoleNameContainingIgnoreCase("cust");

        assertThat(result)
                .extracting(Roles::getRoleName)
                .anyMatch(roleName -> roleName.equalsIgnoreCase("CUSTOMER"));
    }

    @Test
    void findByRoleNameContainingIgnoreCase_WhenKeywordDoesNotMatch_ReturnsEmptyList() {
        List<Roles> result = rolesRepo.findByRoleNameContainingIgnoreCase("xyz");

        assertThat(result).isEmpty();
    }

    @Test
    void existsByRoleNameIgnoreCase_WhenAdminExists_ReturnsTrue() {
        boolean result = rolesRepo.existsByRoleNameIgnoreCase("admin");

        assertThat(result).isTrue();
    }

    @Test
    void existsByRoleNameIgnoreCase_WhenCustomerExists_ReturnsTrue() {
        boolean result = rolesRepo.existsByRoleNameIgnoreCase("customer");

        assertThat(result).isTrue();
    }

    @Test
    void existsByRoleNameIgnoreCase_WhenRoleDoesNotExist_ReturnsFalse() {
        boolean result = rolesRepo.existsByRoleNameIgnoreCase("MANAGER");

        assertThat(result).isFalse();
    }

    @Test
    void findByRoleNameStartingWithIgnoreCase_WhenPrefixAd_ReturnsAdmin() {
        List<Roles> result = rolesRepo.findByRoleNameStartingWithIgnoreCase("ad");

        assertThat(result)
                .extracting(Roles::getRoleName)
                .anyMatch(roleName -> roleName.equalsIgnoreCase("ADMIN"));
    }

    @Test
    void findByRoleNameStartingWithIgnoreCase_WhenPrefixCust_ReturnsCustomer() {
        List<Roles> result = rolesRepo.findByRoleNameStartingWithIgnoreCase("cust");

        assertThat(result)
                .extracting(Roles::getRoleName)
                .anyMatch(roleName -> roleName.equalsIgnoreCase("CUSTOMER"));
    }

    @Test
    void findByRoleNameStartingWithIgnoreCase_WhenPrefixDoesNotMatch_ReturnsEmptyList() {
        List<Roles> result = rolesRepo.findByRoleNameStartingWithIgnoreCase("zz");

        assertThat(result).isEmpty();
    }

    @Test
    void findByRoleIdIn_WhenAdminAndCustomerIdsExist_ReturnsBothRoles() {
        List<Roles> result = rolesRepo.findByRoleIdIn(
                List.of(adminRole.getRoleId(), customerRole.getRoleId())
        );

        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(Roles::getRoleName)
                .anyMatch(roleName -> roleName.equalsIgnoreCase("ADMIN"));

        assertThat(result)
                .extracting(Roles::getRoleName)
                .anyMatch(roleName -> roleName.equalsIgnoreCase("CUSTOMER"));
    }

    @Test
    void findByRoleIdIn_WhenIdsDoNotExist_ReturnsEmptyList() {
        List<Roles> result = rolesRepo.findByRoleIdIn(List.of(999999, 888888));

        assertThat(result).isEmpty();
    }
}