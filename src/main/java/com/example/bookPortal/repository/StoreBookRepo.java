package com.example.bookPortal.repository;

import com.example.bookPortal.entity.StoreBook;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface StoreBookRepo extends JpaRepository<StoreBook, Integer> {

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
