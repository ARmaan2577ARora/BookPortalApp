package com.example.bookPortal.repository;

import com.example.bookPortal.entity.StoreBook;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bookPortal.projection.StoreBookProjection;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "storeBooks", excerptProjection = StoreBookProjection.class)
public interface StoreBookRepo extends JpaRepository<StoreBook, Integer> {

    List<StoreBookProjection> findStoreBookProjectionByStore_StoreId(Integer storeId);

    List<StoreBookProjection> findStoreBookProjectionByBook_BookId(Integer bookId);

    List<StoreBookProjection> findStoreBookProjectionByBook_TitleContainingIgnoreCase(String title);

    List<StoreBookProjection> findStoreBookProjectionByBook_Isbn(String isbn);

    List<StoreBookProjection> findStoreBookProjectionByStore_CityIgnoreCase(String city);

    List<StoreBookProjection> findStoreBookProjectionByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<StoreBookProjection> findStoreBookProjectionByStockQuantityGreaterThan(Integer quantity);
    List<StoreBook> findByStore_StoreId(Integer storeId);

    List<StoreBook> findByBook_BookId(Integer bookId);

    Optional<StoreBook> findByStore_StoreIdAndBook_BookId(Integer storeId, Integer bookId);

    boolean existsByStore_StoreIdAndBook_BookId(Integer storeId, Integer bookId);

    List<StoreBook> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<StoreBook> findByStockQuantityGreaterThan(Integer quantity);

    List<StoreBook> findByStockQuantityLessThanEqual(Integer quantity);

    List<StoreBook> findByDeliveryDaysLessThanEqual(Integer deliveryDays);

    List<StoreBook> findByBook_TitleContainingIgnoreCase(String title);

    List<StoreBook> findByBook_Isbn(String isbn);

    List<StoreBook> findByStore_CityIgnoreCase(String city);

    List<StoreBook> findByBook_BookIdAndStockQuantityGreaterThan(Integer bookId, Integer quantity);
}