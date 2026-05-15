package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Store;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StoreRepoTest {

    @Autowired
    private StoreRepo storeRepo;

    @Test
    void testStoreRepoMethods() {
        String suffix = RepoTestHelper.suffix();

        Store store = storeRepo.save(RepoTestHelper.store(suffix));

        assertThat(storeRepo.findByStoreNameContainingIgnoreCase(suffix))
                .extracting(Store::getStoreId)
                .contains(store.getStoreId());

        assertThat(storeRepo.findByCityIgnoreCase(store.getCity().toLowerCase()))
                .extracting(Store::getStoreId)
                .contains(store.getStoreId());

        assertThat(storeRepo.findByStateIgnoreCase(store.getState().toLowerCase()))
                .extracting(Store::getStoreId)
                .contains(store.getStoreId());

        assertThat(storeRepo.findByCountryIgnoreCase(store.getCountry().toLowerCase()))
                .extracting(Store::getStoreId)
                .contains(store.getStoreId());

        Optional<Store> byWebsite = storeRepo.findByWebsite(store.getWebsite());
        assertThat(byWebsite).isPresent();

        assertThat(storeRepo.existsByWebsite(store.getWebsite())).isTrue();

        assertThat(storeRepo.findByRatingGreaterThanEqual(new BigDecimal("4.00")))
                .extracting(Store::getStoreId)
                .contains(store.getStoreId());

        assertThat(storeRepo.findByRatingBetween(new BigDecimal("4.00"), new BigDecimal("5.00")))
                .extracting(Store::getStoreId)
                .contains(store.getStoreId());
    }
}