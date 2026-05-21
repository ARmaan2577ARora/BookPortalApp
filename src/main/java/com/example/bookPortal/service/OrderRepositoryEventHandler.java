package com.example.bookPortal.service;

import com.example.bookPortal.entity.Order;
import com.example.bookPortal.exception.BadRequestException;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RepositoryEventHandler(Order.class)
public class OrderRepositoryEventHandler {

    @HandleBeforeCreate
    public void handleBeforeCreate(Order order) {
        validateOrder(order);

        LocalDateTime now = LocalDateTime.now();

        if (order.getOrderDate() == null) {
            order.setOrderDate(now);
        }

        if (order.getCreatedAt() == null) {
            order.setCreatedAt(now);
        }

        order.setUpdatedAt(now);

        if (order.getOrderStatus() == null || order.getOrderStatus().isBlank()) {
            order.setOrderStatus("PROCESSING");
        }

        if (order.getTotalAmount() == null) {
            order.setTotalAmount(BigDecimal.ZERO);
        }
    }

    @HandleBeforeSave
    public void handleBeforeSave(Order order) {
        validateOrder(order);
        order.setUpdatedAt(LocalDateTime.now());
    }

    private void validateOrder(Order order) {
        if (order.getUser() == null || order.getUser().getUserId() == null) {
            throw new BadRequestException("User is required for order");
        }

        if (order.getAddress() == null || order.getAddress().getAddressId() == null) {
            throw new BadRequestException("Address is required for order");
        }

        if (order.getPaymentMethod() == null || order.getPaymentMethod().isBlank()) {
            throw new BadRequestException("Payment method is required");
        }
    }
}