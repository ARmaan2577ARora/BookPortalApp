package com.example.bookPortal.projection;

import com.example.bookPortal.entity.Store;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

@Projection(name = "storeView", types = Store.class)
public interface StoreProjection {

    Integer getStoreId();

    String getStoreName();

    String getCity();

    String getState();

    String getCountry();

    String getWebsite();

    BigDecimal getRating();
}