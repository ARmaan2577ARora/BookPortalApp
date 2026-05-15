package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Address;
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
class AddressRepoTest {

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private RolesRepo rolesRepo;

    private Roles getOrCreateRole(String roleName) {
        return rolesRepo.findByRoleNameIgnoreCase(roleName)
                .orElseGet(() -> rolesRepo.save(RepoTestHelper.role(roleName)));
    }

    private TestData createTestData() {
        String suffix = RepoTestHelper.suffix();

        Roles customer = getOrCreateRole("CUSTOMER");

        User user = userRepo.save(
                RepoTestHelper.user(suffix, customer, true)
        );

        Address defaultAddress = addressRepo.save(
                RepoTestHelper.address(user, suffix, true)
        );

        Address otherAddress = addressRepo.save(
                RepoTestHelper.address(user, "other_" + suffix, false)
        );

        return new TestData(user, defaultAddress, otherAddress, suffix);
    }

    @Test
    void findByUser_UserId_WhenUserExists_ReturnsAddresses() {
        TestData data = createTestData();

        List<Address> result = addressRepo.findByUser_UserId(data.user.getUserId());

        assertThat(result)
                .extracting(Address::getAddressId)
                .contains(data.defaultAddress.getAddressId(), data.otherAddress.getAddressId());
    }

    @Test
    void findByUser_UserId_WhenUserDoesNotExist_ReturnsEmptyList() {
        List<Address> result = addressRepo.findByUser_UserId(Integer.MAX_VALUE);

        assertThat(result).isEmpty();
    }

    @Test
    void findByUser_UserIdAndIsDefaultTrue_WhenDefaultAddressExists_ReturnsDefaultAddress() {
        TestData data = createTestData();

        Optional<Address> result = addressRepo.findByUser_UserIdAndIsDefaultTrue(data.user.getUserId());

        assertThat(result).isPresent();
        assertThat(result.get().getAddressId()).isEqualTo(data.defaultAddress.getAddressId());
    }

    @Test
    void findByUser_UserIdAndIsDefaultTrue_WhenDefaultAddressDoesNotExist_ReturnsEmptyOptional() {
        String suffix = RepoTestHelper.suffix();

        Roles customer = getOrCreateRole("CUSTOMER");

        User user = userRepo.save(
                RepoTestHelper.user("nodefault_" + suffix, customer, true)
        );

        addressRepo.save(
                RepoTestHelper.address(user, "nd_" + suffix, false)
        );

        Optional<Address> result = addressRepo.findByUser_UserIdAndIsDefaultTrue(user.getUserId());

        assertThat(result).isEmpty();
    }

    @Test
    void findByCityIgnoreCase_WhenCityExists_ReturnsAddresses() {
        TestData data = createTestData();

        List<Address> result = addressRepo.findByCityIgnoreCase(data.defaultAddress.getCity().toLowerCase());

        assertThat(result)
                .extracting(Address::getAddressId)
                .contains(data.defaultAddress.getAddressId());
    }

    @Test
    void findByCityIgnoreCase_WhenCityDoesNotExist_ReturnsEmptyList() {
        String wrongCity = "NO_CITY_" + RepoTestHelper.suffix();

        List<Address> result = addressRepo.findByCityIgnoreCase(wrongCity);

        assertThat(result).isEmpty();
    }

    @Test
    void findByStateIgnoreCase_WhenStateExists_ReturnsAddresses() {
        TestData data = createTestData();

        List<Address> result = addressRepo.findByStateIgnoreCase(data.defaultAddress.getState().toLowerCase());

        assertThat(result)
                .extracting(Address::getAddressId)
                .contains(data.defaultAddress.getAddressId());
    }

    @Test
    void findByStateIgnoreCase_WhenStateDoesNotExist_ReturnsEmptyList() {
        String wrongState = "NO_STATE_" + RepoTestHelper.suffix();

        List<Address> result = addressRepo.findByStateIgnoreCase(wrongState);

        assertThat(result).isEmpty();
    }

    @Test
    void findByCountryIgnoreCase_WhenCountryExists_ReturnsAddresses() {
        TestData data = createTestData();

        List<Address> result = addressRepo.findByCountryIgnoreCase(data.defaultAddress.getCountry().toLowerCase());

        assertThat(result)
                .extracting(Address::getAddressId)
                .contains(data.defaultAddress.getAddressId());
    }

    @Test
    void findByCountryIgnoreCase_WhenCountryDoesNotExist_ReturnsEmptyList() {
        String wrongCountry = "NO_COUNTRY_" + RepoTestHelper.suffix();

        List<Address> result = addressRepo.findByCountryIgnoreCase(wrongCountry);

        assertThat(result).isEmpty();
    }

    @Test
    void findByZipcode_WhenZipcodeExists_ReturnsAddresses() {
        TestData data = createTestData();

        List<Address> result = addressRepo.findByZipcode(data.defaultAddress.getZipcode());

        assertThat(result)
                .extracting(Address::getAddressId)
                .contains(data.defaultAddress.getAddressId());
    }

    @Test
    void findByZipcode_WhenZipcodeDoesNotExist_ReturnsEmptyList() {
        String wrongZipcode = "NO_ZIP_" + RepoTestHelper.suffix();

        List<Address> result = addressRepo.findByZipcode(wrongZipcode);

        assertThat(result).isEmpty();
    }

    @Test
    void findByUser_UserIdAndCityIgnoreCase_WhenUserAndCityExist_ReturnsAddresses() {
        TestData data = createTestData();

        List<Address> result = addressRepo.findByUser_UserIdAndCityIgnoreCase(
                data.user.getUserId(),
                data.defaultAddress.getCity().toLowerCase()
        );

        assertThat(result)
                .extracting(Address::getAddressId)
                .contains(data.defaultAddress.getAddressId());
    }

    @Test
    void findByUser_EmailIgnoreCase_WhenEmailExists_ReturnsAddresses() {
        TestData data = createTestData();

        List<Address> result = addressRepo.findByUser_EmailIgnoreCase(data.user.getEmail().toUpperCase());

        assertThat(result)
                .extracting(Address::getAddressId)
                .contains(data.defaultAddress.getAddressId(), data.otherAddress.getAddressId());
    }

    private static class TestData {

        private final User user;
        private final Address defaultAddress;
        private final Address otherAddress;
        private final String suffix;

        private TestData(User user, Address defaultAddress, Address otherAddress, String suffix) {
            this.user = user;
            this.defaultAddress = defaultAddress;
            this.otherAddress = otherAddress;
            this.suffix = suffix;
        }
    }
}
