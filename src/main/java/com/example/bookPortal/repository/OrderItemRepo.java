package com.example.bookPortal.repository;

import com.example.bookPortal.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bookPortal.projection.OrderItemProjection;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigDecimal;
import java.util.List;

@RepositoryRestResource(path = "orderItems", excerptProjection = OrderItemProjection.class)
public interface OrderItemRepo extends JpaRepository<OrderItem, Integer> {

    List<OrderItemProjection> findOrderItemProjectionByOrder_OrderId(Integer orderId);

    List<OrderItemProjection> findOrderItemProjectionByStoreBook_Book_BookId(Integer bookId);

    List<OrderItemProjection> findOrderItemProjectionByStoreBook_Store_StoreId(Integer storeId);

    List<OrderItemProjection> findOrderItemProjectionByStoreBook_Book_TitleContainingIgnoreCase(String title);

    List<OrderItemProjection> findOrderItemProjectionByOrder_User_UserId(Integer userId);
    List<OrderItem> findByOrder_OrderId(Integer orderId);

    List<OrderItem> findByStoreBook_StoreBookId(Integer storeBookId);

    List<OrderItem> findByStoreBook_Book_BookId(Integer bookId);

    List<OrderItem> findByStoreBook_Store_StoreId(Integer storeId);

    List<OrderItem> findByQuantityGreaterThan(Integer quantity);

    List<OrderItem> findByBookPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);

    List<OrderItem> findBySubtotalBetween(BigDecimal minSubtotal, BigDecimal maxSubtotal);

    List<OrderItem> findByStoreBook_Book_TitleContainingIgnoreCase(String title);

    List<OrderItem> findByOrder_User_UserId(Integer userId);

    List<OrderItem> findByOrder_OrderStatusIgnoreCase(String orderStatus);
}