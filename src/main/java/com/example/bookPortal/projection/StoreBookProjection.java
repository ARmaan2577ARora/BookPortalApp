package com.example.bookPortal.projection;

import com.example.bookPortal.entity.StoreBook;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Projection(name = "storeBookView", types = StoreBook.class)
public interface StoreBookProjection {

    Integer getStoreBookId();

    BigDecimal getPrice();

    Integer getStockQuantity();

    Integer getDeliveryDays();

    String getStoreBookUrl();

    LocalDateTime getCreatedAt();

    StoreInfo getStore();

    BookInfo getBook();

    interface StoreInfo {

        Integer getStoreId();

        String getStoreName();

        String getCity();

        String getState();

        String getCountry();

        BigDecimal getRating();
    }

    interface BookInfo {

        Integer getBookId();

        String getTitle();

        String getIsbn();

        String getLanguage();

        Integer getPages();
    }
}