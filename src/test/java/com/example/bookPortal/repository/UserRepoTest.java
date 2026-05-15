package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Roles;
import com.example.bookPortal.entity.User;
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
class UserRepoTest {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RolesRepo rolesRepo;

    private Roles getOrCreateRole(String roleName) {
        return rolesRepo.findByRoleNameIgnoreCase(roleName)
                .orElseGet(() -> rolesRepo.save(RepoTestHelper.role(roleName)));
    }

    private User createAndSaveUser(String suffix, Roles role, Boolean active) {
        return userRepo.save(RepoTestHelper.user(suffix, role, active));
    }

    @Test
    void findByEmailIgnoreCase_WhenEmailExists_ReturnsUser() {
        String suffix = RepoTestHelper.suffix();
        Roles customer = getOrCreateRole("CUSTOMER");

        User user = createAndSaveUser(suffix, customer, true);

        Optional<User> result = userRepo.findByEmailIgnoreCase(user.getEmail().toUpperCase());

        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void findByEmailIgnoreCase_WhenEmailDoesNotExist_ReturnsEmptyOptional() {
        String wrongEmail = "wrong_" + RepoTestHelper.suffix() + "@test.com";

        Optional<User> result = userRepo.findByEmailIgnoreCase(wrongEmail);

        assertThat(result).isEmpty();
    }

    @Test
    void existsByEmailIgnoreCase_WhenEmailExists_ReturnsTrue() {
        String suffix = RepoTestHelper.suffix();
        Roles customer = getOrCreateRole("CUSTOMER");

        User user = createAndSaveUser(suffix, customer, true);

        boolean result = userRepo.existsByEmailIgnoreCase(user.getEmail().toUpperCase());

        assertThat(result).isTrue();
    }

    @Test
    void existsByEmailIgnoreCase_WhenEmailDoesNotExist_ReturnsFalse() {
        String wrongEmail = "notfound_" + RepoTestHelper.suffix() + "@test.com";

        boolean result = userRepo.existsByEmailIgnoreCase(wrongEmail);

        assertThat(result).isFalse();
    }

    @Test
    void findByPhone_WhenPhoneExists_ReturnsUser() {
        String suffix = RepoTestHelper.suffix();
        Roles customer = getOrCreateRole("CUSTOMER");

        User user = createAndSaveUser(suffix, customer, true);

        Optional<User> result = userRepo.findByPhone(user.getPhone());

        assertThat(result).isPresent();
        assertThat(result.get().getPhone()).isEqualTo(user.getPhone());
    }

    @Test
    void existsByPhone_WhenPhoneExists_ReturnsTrue() {
        String suffix = RepoTestHelper.suffix();
        Roles customer = getOrCreateRole("CUSTOMER");

        User user = createAndSaveUser(suffix, customer, true);

        boolean result = userRepo.existsByPhone(user.getPhone());

        assertThat(result).isTrue();
    }

    @Test
    void findByFullNameContainingIgnoreCase_WhenNameMatches_ReturnsUsers() {
        String suffix = RepoTestHelper.suffix();
        Roles customer = getOrCreateRole("CUSTOMER");

        User user = createAndSaveUser(suffix, customer, true);

        List<User> result = userRepo.findByFullNameContainingIgnoreCase(suffix.toLowerCase());

        assertThat(result)
                .extracting(User::getEmail)
                .contains(user.getEmail());
    }

    @Test
    void findByIsActiveTrue_WhenActiveUserExists_ReturnsActiveUsers() {
        String suffix = RepoTestHelper.suffix();
        Roles customer = getOrCreateRole("CUSTOMER");

        User activeUser = createAndSaveUser(suffix, customer, true);

        List<User> result = userRepo.findByIsActiveTrue();

        assertThat(result)
                .extracting(User::getEmail)
                .contains(activeUser.getEmail());
    }

    @Test
    void findByIsActiveFalse_WhenInactiveUserExists_ReturnsInactiveUsers() {
        String suffix = RepoTestHelper.suffix();
        Roles customer = getOrCreateRole("CUSTOMER");

        User inactiveUser = createAndSaveUser("inactive_" + suffix, customer, false);

        List<User> result = userRepo.findByIsActiveFalse();

        assertThat(result)
                .extracting(User::getEmail)
                .contains(inactiveUser.getEmail());
    }

    @Test
    void findByRole_RoleId_WhenRoleExists_ReturnsUsers() {
        String suffix = RepoTestHelper.suffix();
        Roles customer = getOrCreateRole("CUSTOMER");

        User user = createAndSaveUser(suffix, customer, true);

        List<User> result = userRepo.findByRole_RoleId(customer.getRoleId());

        assertThat(result)
                .extracting(User::getEmail)
                .contains(user.getEmail());
    }

    @Test
    void findByRole_RoleNameIgnoreCase_WhenRoleExists_ReturnsUsers() {
        String suffix = RepoTestHelper.suffix();
        Roles admin = getOrCreateRole("ADMIN");

        User user = createAndSaveUser(suffix, admin, true);

        List<User> result = userRepo.findByRole_RoleNameIgnoreCase("admin");

        assertThat(result)
                .extracting(User::getEmail)
                .contains(user.getEmail());
    }
}