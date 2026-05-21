package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bookPortal.projection.StoreProjection;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "stores", excerptProjection = StoreProjection.class)
public interface StoreRepo extends JpaRepository<Store, Integer> {

    List<StoreProjection> findStoreProjectionByStoreNameContainingIgnoreCase(String storeName);

    List<StoreProjection> findStoreProjectionByCityIgnoreCase(String city);

    List<StoreProjection> findStoreProjectionByCountryIgnoreCase(String country);

    List<StoreProjection> findStoreProjectionByRatingGreaterThanEqual(BigDecimal rating);
    List<Store> findByStoreNameContainingIgnoreCase(String storeName);

    List<Store> findByCityIgnoreCase(String city);

    List<Store> findByStateIgnoreCase(String state);

    List<Store> findByCountryIgnoreCase(String country);

    Optional<Store> findByWebsite(String website);

    boolean existsByWebsite(String website);

    List<Store> findByRatingGreaterThanEqual(BigDecimal rating);

    List<Store> findByRatingBetween(BigDecimal minRating, BigDecimal maxRating);
}