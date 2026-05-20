package com.example.bookPortal.service;

import com.example.bookPortal.entity.Order;
import com.example.bookPortal.entity.OrderItem;
import com.example.bookPortal.entity.StoreBook;
import com.example.bookPortal.repository.OrderItemRepo;
import com.example.bookPortal.repository.StoreBookRepo;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Set;

@Service
public class OrderStockService {
    private static final String CANCELLED = "CANCELLED";
    private static final String SHIPPED = "SHIPPED";
    private static final String DELIVERED = "DELIVERED";
    private static final Set<String> VALID_STATUSES = Set.of("PROCESSING", "CONFIRMED", "SHIPPED", "DELIVERED", "CANCELLED");

    private final OrderItemRepo orderItemRepo;
    private final StoreBookRepo storeBookRepo;

    public OrderStockService(OrderItemRepo orderItemRepo, StoreBookRepo storeBookRepo) {
        this.orderItemRepo = orderItemRepo;
        this.storeBookRepo = storeBookRepo;
    }

    @Transactional
    public void changeStatusAndAdjustStock(Order order, String requestedStatus) {
        String oldStatus = normalize(order.getOrderStatus());
        String newStatus = normalize(requestedStatus);

        if (newStatus.isBlank()) {
            newStatus = "PROCESSING";
        }

        if (!VALID_STATUSES.contains(newStatus)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid order status: " + requestedStatus);
        }

        if (CANCELLED.equals(newStatus) && (SHIPPED.equals(oldStatus) || DELIVERED.equals(oldStatus))) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, oldStatus + " order cannot be cancelled");
        }

        if (!CANCELLED.equals(oldStatus) && CANCELLED.equals(newStatus)) {
            restoreStock(order);
        }

        if (CANCELLED.equals(oldStatus) && !CANCELLED.equals(newStatus)) {
            reduceStockAgain(order);
        }

        order.setOrderStatus(newStatus);
        order.setUpdatedAt(LocalDateTime.now());
    }

    @Transactional
    public void cancelCustomerOrder(Order order) {
        String currentStatus = normalize(order.getOrderStatus());

        if (CANCELLED.equals(currentStatus)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Order is already cancelled");
        }

        if (SHIPPED.equals(currentStatus) || DELIVERED.equals(currentStatus)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, currentStatus + " order cannot be cancelled");
        }

        changeStatusAndAdjustStock(order, CANCELLED);
    }

    private void restoreStock(Order order) {
        for (OrderItem item : orderItemRepo.findByOrder_OrderId(order.getOrderId())) {
            StoreBook storeBook = item.getStoreBook();
            if (storeBook == null) continue;

            int currentStock = storeBook.getStockQuantity() == null ? 0 : storeBook.getStockQuantity();
            int orderedQuantity = item.getQuantity() == null ? 0 : item.getQuantity();

            storeBook.setStockQuantity(currentStock + orderedQuantity);
            storeBookRepo.save(storeBook);
        }
    }

    private void reduceStockAgain(Order order) {
        for (OrderItem item : orderItemRepo.findByOrder_OrderId(order.getOrderId())) {
            StoreBook storeBook = item.getStoreBook();
            if (storeBook == null) continue;

            int currentStock = storeBook.getStockQuantity() == null ? 0 : storeBook.getStockQuantity();
            int orderedQuantity = item.getQuantity() == null ? 0 : item.getQuantity();

            if (currentStock < orderedQuantity) {
                String bookName = storeBook.getBook() == null ? "selected book" : storeBook.getBook().getTitle();
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not enough stock to reactivate order for " + bookName);
            }

            storeBook.setStockQuantity(currentStock - orderedQuantity);
            storeBookRepo.save(storeBook);
        }
    }

    private String normalize(String status) {
        return status == null ? "" : status.trim().toUpperCase();
    }
}
