package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import com.example.bookPortal.projection.OrderProjection;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@RepositoryRestResource(path = "orders", excerptProjection = OrderProjection.class)
public interface OrderRepo extends JpaRepository<Order, Integer> {

    List<OrderProjection> findOrderProjectionByUser_UserId(Integer userId);

    List<OrderProjection> findOrderProjectionByOrderStatusIgnoreCase(String orderStatus);

    List<OrderProjection> findOrderProjectionByPaymentMethodIgnoreCase(String paymentMethod);

    List<OrderProjection> findOrderProjectionByUser_EmailIgnoreCase(String email);

    List<OrderProjection> findTop10OrderProjectionByUser_UserIdOrderByOrderDateDesc(Integer userId);
    List<Order> findByUser_UserId(Integer userId);

    List<Order> findByAddress_AddressId(Integer addressId);

    List<Order> findByOrderStatusIgnoreCase(String orderStatus);

    List<Order> findByPaymentMethodIgnoreCase(String paymentMethod);

    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<Order> findByTotalAmountBetween(BigDecimal minAmount, BigDecimal maxAmount);

    List<Order> findByUser_EmailIgnoreCase(String email);

    List<Order> findByUser_FullNameContainingIgnoreCase(String fullName);

    List<Order> findTop10ByUser_UserIdOrderByOrderDateDesc(Integer userId);

    List<Order> findByUser_UserIdAndOrderStatusIgnoreCase(Integer userId, String orderStatus);
}