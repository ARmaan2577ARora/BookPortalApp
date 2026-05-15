package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Roles;
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

    private Roles getOrCreateRole(String roleName) {
        return rolesRepo.findByRoleNameIgnoreCase(roleName)
                .orElseGet(() -> rolesRepo.save(RepoTestHelper.role(roleName)));
    }

    @Test
    void findByRoleNameIgnoreCase_WhenAdminExists_ReturnsAdmin() {
        getOrCreateRole("ADMIN");

        Optional<Roles> result = rolesRepo.findByRoleNameIgnoreCase("admin");

        assertThat(result).isPresent();
        assertThat(result.get().getRoleName()).isEqualToIgnoringCase("ADMIN");
    }

    @Test
    void findByRoleNameIgnoreCase_WhenCustomerExists_ReturnsCustomer() {
        getOrCreateRole("CUSTOMER");

        Optional<Roles> result = rolesRepo.findByRoleNameIgnoreCase("customer");

        assertThat(result).isPresent();
        assertThat(result.get().getRoleName()).isEqualToIgnoringCase("CUSTOMER");
    }

    @Test
    void findByRoleNameIgnoreCase_WhenRoleDoesNotExist_ReturnsEmpty() {
        Optional<Roles> result = rolesRepo.findByRoleNameIgnoreCase("NOT_EXISTING_ROLE");

        assertThat(result).isEmpty();
    }

    @Test
    void findByRoleNameContainingIgnoreCase_WhenAdm_ReturnsAdmin() {
        getOrCreateRole("ADMIN");

        List<Roles> result = rolesRepo.findByRoleNameContainingIgnoreCase("adm");

        assertThat(result)
                .extracting(Roles::getRoleName)
                .anyMatch(name -> name.equalsIgnoreCase("ADMIN"));
    }

    @Test
    void findByRoleNameContainingIgnoreCase_WhenCust_ReturnsCustomer() {
        getOrCreateRole("CUSTOMER");

        List<Roles> result = rolesRepo.findByRoleNameContainingIgnoreCase("cust");

        assertThat(result)
                .extracting(Roles::getRoleName)
                .anyMatch(name -> name.equalsIgnoreCase("CUSTOMER"));
    }

    @Test
    void existsByRoleNameIgnoreCase_WhenAdminExists_ReturnsTrue() {
        getOrCreateRole("ADMIN");

        boolean result = rolesRepo.existsByRoleNameIgnoreCase("admin");

        assertThat(result).isTrue();
    }

    @Test
    void existsByRoleNameIgnoreCase_WhenCustomerExists_ReturnsTrue() {
        getOrCreateRole("CUSTOMER");

        boolean result = rolesRepo.existsByRoleNameIgnoreCase("customer");

        assertThat(result).isTrue();
    }

    @Test
    void existsByRoleNameIgnoreCase_WhenManagerDoesNotExist_ReturnsFalse() {
        boolean result = rolesRepo.existsByRoleNameIgnoreCase("manager");

        assertThat(result).isFalse();
    }

    @Test
    void findByRoleIdIn_WhenAdminAndCustomerIdsExist_ReturnsBoth() {
        Roles admin = getOrCreateRole("ADMIN");
        Roles customer = getOrCreateRole("CUSTOMER");

        List<Roles> result = rolesRepo.findByRoleIdIn(
                List.of(admin.getRoleId(), customer.getRoleId())
        );

        assertThat(result).hasSizeGreaterThanOrEqualTo(2);
        assertThat(result)
                .extracting(Roles::getRoleName)
                .anyMatch(name -> name.equalsIgnoreCase("ADMIN"));

        assertThat(result)
                .extracting(Roles::getRoleName)
                .anyMatch(name -> name.equalsIgnoreCase("CUSTOMER"));
    }
}