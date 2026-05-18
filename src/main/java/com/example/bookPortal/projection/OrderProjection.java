package com.example.bookPortal.projection;

import com.example.bookPortal.entity.Order;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Projection(name = "orderView", types = Order.class)
public interface OrderProjection {

    Integer getOrderId();

    LocalDateTime getOrderDate();

    String getPaymentMethod();

    String getOrderStatus();

    BigDecimal getTotalAmount();

    LocalDateTime getCreatedAt();

    LocalDateTime getUpdatedAt();

    UserInfo getUser();

    AddressInfo getAddress();

    interface UserInfo {

        Integer getUserId();

        String getFullName();

        String getEmail();

        String getPhone();
    }

    interface AddressInfo {

        Integer getAddressId();

        String getHouseAddress();

        String getCity();

        String getState();

        String getZipcode();

        String getCountry();
    }
}
