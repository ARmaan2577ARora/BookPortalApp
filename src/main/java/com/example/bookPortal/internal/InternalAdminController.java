package com.example.bookPortal.internal;

import com.example.bookPortal.entity.Order;
import com.example.bookPortal.entity.StoreBook;
import com.example.bookPortal.repository.*;
import com.example.bookPortal.service.OrderStockService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/internal/admin")
public class InternalAdminController {
    private final BookRepo bookRepo;
    private final AuthorRepo authorRepo;
    private final PublisherRepo publisherRepo;
    private final StoreRepo storeRepo;
    private final StoreBookRepo storeBookRepo;
    private final OrderRepo orderRepo;
    private final UserRepo userRepo;
    private final OrderItemRepo orderItemRepo;
    private final OrderStockService orderStockService;

    public InternalAdminController(BookRepo bookRepo, AuthorRepo authorRepo, PublisherRepo publisherRepo, StoreRepo storeRepo,
                                   StoreBookRepo storeBookRepo, OrderRepo orderRepo, UserRepo userRepo,
                                   OrderItemRepo orderItemRepo, OrderStockService orderStockService) {
        this.bookRepo = bookRepo;
        this.authorRepo = authorRepo;
        this.publisherRepo = publisherRepo;
        this.storeRepo = storeRepo;
        this.storeBookRepo = storeBookRepo;
        this.orderRepo = orderRepo;
        this.userRepo = userRepo;
        this.orderItemRepo = orderItemRepo;
        this.orderStockService = orderStockService;
    }

    @GetMapping("/analytics")
    public Map<String, Object> analytics() {
        BigDecimal revenue = orderRepo.findAll().stream()
                .filter(order -> !"CANCELLED".equalsIgnoreCase(order.getOrderStatus()))
                .map(Order::getTotalAmount)
                .filter(v -> v != null)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return Map.of(
                "bookCount", bookRepo.count(),
                "authorCount", authorRepo.count(),
                "publisherCount", publisherRepo.count(),
                "storeCount", storeRepo.count(),
                "orderCount", orderRepo.count(),
                "customerCount", userRepo.findByRole_RoleNameIgnoreCase("CUSTOMER").size(),
                "revenue", revenue,
                "lowStock", storeBookRepo.findByStockQuantityLessThanEqual(5).stream().map(BackendMapper::storeBook).toList(),
                "recentOrders", orderRepo.findAll().stream().sorted(Comparator.comparing(Order::getOrderDate, Comparator.nullsLast(Comparator.reverseOrder()))).limit(10).map(order -> {
                    Map<String, Object> map = new LinkedHashMap<>(BackendMapper.order(order));
                    map.put("items", orderItemRepo.findByOrder_OrderId(order.getOrderId()).stream().map(BackendMapper::orderItem).toList());
                    return map;
                }).toList()
        );
    }

    @PostMapping("/orders/{orderId}/status")
    public Map<String, Object> updateStatus(@PathVariable Integer orderId, @RequestBody Map<String, Object> request) {
        Order order = orderRepo.findById(orderId).orElseThrow();
        orderStockService.changeStatusAndAdjustStock(order, String.valueOf(request.getOrDefault("status", "PROCESSING")));
        return Map.of("order", BackendMapper.order(orderRepo.save(order)));
    }
}
