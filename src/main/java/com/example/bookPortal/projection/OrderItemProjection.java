package com.example.bookPortal.projection;

import com.example.bookPortal.entity.OrderItem;
import org.springframework.data.rest.core.config.Projection;

import java.math.BigDecimal;

@Projection(name = "orderItemView", types = OrderItem.class)
public interface OrderItemProjection {

    Integer getOrderItemId();

    Integer getQuantity();

    BigDecimal getBookPrice();

    BigDecimal getSubtotal();

    OrderInfo getOrder();

    StoreBookInfo getStoreBook();

    interface OrderInfo {

        Integer getOrderId();

        String getOrderStatus();

        BigDecimal getTotalAmount();
    }

    interface StoreBookInfo {

        Integer getStoreBookId();

        BigDecimal getPrice();

        Integer getStockQuantity();

        Integer getDeliveryDays();

        BookInfo getBook();

        StoreInfo getStore();
    }

    interface BookInfo {

        Integer getBookId();

        String getTitle();

        String getIsbn();
    }

    interface StoreInfo {

        Integer getStoreId();

        String getStoreName();

        String getCity();

        BigDecimal getRating();
    }
}
