package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


public interface StoreRepo extends JpaRepository<Store, Integer> {
    List<Store> findByStoreNameContainingIgnoreCase(String storeName);

    List<Store> findByCityIgnoreCase(String city);

    List<Store> findByStateIgnoreCase(String state);

    List<Store> findByCountryIgnoreCase(String country);

    Optional<Store> findByWebsite(String website);

    boolean existsByWebsite(String website);

    List<Store> findByRatingGreaterThanEqual(BigDecimal rating);

    List<Store> findByRatingBetween(BigDecimal minRating, BigDecimal maxRating);
}
