package com.example.bookPortal.service;


import com.example.bookPortal.entity.Order;
import com.example.bookPortal.entity.OrderItem;
import com.example.bookPortal.entity.StoreBook;
import com.example.bookPortal.exception.BadRequestException;
import com.example.bookPortal.exception.NotFoundException;
import com.example.bookPortal.repository.OrderItemRepo;
import com.example.bookPortal.repository.OrderRepo;
import com.example.bookPortal.repository.StoreBookRepo;
import org.springframework.data.rest.core.annotation.HandleAfterCreate;
import org.springframework.data.rest.core.annotation.HandleAfterDelete;
import org.springframework.data.rest.core.annotation.HandleAfterSave;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

@Component
@RepositoryEventHandler(OrderItem.class)
public class OrderItemRepositoryEventHandler {

    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;
    private final StoreBookRepo storeBookRepo;

    public OrderItemRepositoryEventHandler(OrderRepo orderRepo,
                                           OrderItemRepo orderItemRepo,
                                           StoreBookRepo storeBookRepo) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.storeBookRepo = storeBookRepo;
    }

    @HandleBeforeCreate
    public void handleBeforeCreate(OrderItem orderItem) {
        validateAndCalculate(orderItem);
        reduceStock(orderItem);
    }

    @HandleBeforeSave
    public void handleBeforeSave(OrderItem orderItem) {
        validateAndCalculate(orderItem);
    }

    @HandleAfterCreate
    public void handleAfterCreate(OrderItem orderItem) {
        recalculateOrderTotal(orderItem.getOrder());
    }

    @HandleAfterSave
    public void handleAfterSave(OrderItem orderItem) {
        recalculateOrderTotal(orderItem.getOrder());
    }

    @HandleAfterDelete
    public void handleAfterDelete(OrderItem orderItem) {
        recalculateOrderTotal(orderItem.getOrder());
    }

    private void validateAndCalculate(OrderItem orderItem) {
        if (orderItem.getOrder() == null || orderItem.getOrder().getOrderId() == null) {
            throw new BadRequestException("Order is required for order item");
        }

        if (orderItem.getStoreBook() == null || orderItem.getStoreBook().getStoreBookId() == null) {
            throw new BadRequestException("Store book is required for order item");
        }

        if (orderItem.getQuantity() == null || orderItem.getQuantity() <= 0) {
            throw new BadRequestException("Quantity must be greater than 0");
        }

        StoreBook storeBook = storeBookRepo
                .findById(orderItem.getStoreBook().getStoreBookId())
                .orElseThrow(() -> new NotFoundException("Store book not found"));

        if (storeBook.getPrice() == null) {
            throw new BadRequestException("Book price is missing");
        }

        if (storeBook.getStockQuantity() == null || storeBook.getStockQuantity() < orderItem.getQuantity()) {
            throw new BadRequestException("Not enough stock available");
        }

        BigDecimal subtotal = storeBook.getPrice()
                .multiply(BigDecimal.valueOf(orderItem.getQuantity()));

        orderItem.setStoreBook(storeBook);
        orderItem.setBookPrice(storeBook.getPrice());
        orderItem.setSubtotal(subtotal);
    }

    private void reduceStock(OrderItem orderItem) {
        StoreBook storeBook = orderItem.getStoreBook();

        int remainingStock = storeBook.getStockQuantity() - orderItem.getQuantity();

        storeBook.setStockQuantity(remainingStock);
        storeBookRepo.save(storeBook);
    }

    private void recalculateOrderTotal(Order order) {
        if (order == null || order.getOrderId() == null) {
            return;
        }

        Order dbOrder = orderRepo.findById(order.getOrderId())
                .orElseThrow(() -> new NotFoundException("Order not found"));

        BigDecimal total = orderItemRepo.findByOrder_OrderId(dbOrder.getOrderId())
                .stream()
                .map(OrderItem::getSubtotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        dbOrder.setTotalAmount(total);
        dbOrder.setUpdatedAt(LocalDateTime.now());

        orderRepo.save(dbOrder);
    }
}