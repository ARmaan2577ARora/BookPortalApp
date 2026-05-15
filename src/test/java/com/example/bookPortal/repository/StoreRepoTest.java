package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Store;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;
import org.junit.jupiter.api.BeforeEach;
import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StoreRepoTest {

    @Autowired
    private StoreRepo storeRepo;

    private Store store;
    private String suffix;

    @BeforeEach
    void setUp() {
        suffix = RepoTestHelper.suffix();
        store = storeRepo.save(RepoTestHelper.store(suffix));
    }

    @Test
    void shouldFindByStoreNameContainingIgnoreCase() {
        assertThat(storeRepo.findByStoreNameContainingIgnoreCase(suffix))
                .extracting(Store::getStoreId)
                .contains(store.getStoreId());
    }

    @Test
    void shouldFindByCityIgnoreCase() {
        assertThat(storeRepo.findByCityIgnoreCase(store.getCity().toLowerCase()))
                .extracting(Store::getStoreId)
                .contains(store.getStoreId());
    }

    @Test
    void shouldFindByStateIgnoreCase() {
        assertThat(storeRepo.findByStateIgnoreCase(store.getState().toLowerCase()))
                .extracting(Store::getStoreId)
                .contains(store.getStoreId());
    }

    @Test
    void shouldFindByCountryIgnoreCase() {
        assertThat(storeRepo.findByCountryIgnoreCase(store.getCountry().toLowerCase()))
                .extracting(Store::getStoreId)
                .contains(store.getStoreId());
    }

    @Test
    void shouldFindByWebsite() {
        Optional<Store> result =
                storeRepo.findByWebsite(store.getWebsite());

        assertThat(result).isPresent();
        assertThat(result.get().getStoreId())
                .isEqualTo(store.getStoreId());
    }

    @Test
    void shouldCheckIfWebsiteExists() {
        assertThat(storeRepo.existsByWebsite(store.getWebsite()))
                .isTrue();
    }

    @Test
    void shouldFindByRatingGreaterThanEqual() {
        assertThat(storeRepo.findByRatingGreaterThanEqual(
                new BigDecimal("4.00")
        ))
                .extracting(Store::getStoreId)
                .contains(store.getStoreId());
    }

    @Test
    void shouldFindByRatingBetween() {
        assertThat(storeRepo.findByRatingBetween(
                new BigDecimal("4.00"),
                new BigDecimal("5.00")
        ))
                .extracting(Store::getStoreId)
                .contains(store.getStoreId());
    }
}