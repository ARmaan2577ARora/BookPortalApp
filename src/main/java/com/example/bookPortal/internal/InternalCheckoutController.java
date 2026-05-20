package com.example.bookPortal.internal;

import com.example.bookPortal.entity.*;
import com.example.bookPortal.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/internal/checkout")
public class InternalCheckoutController {
    private final UserRepo userRepo;
    private final AddressRepo addressRepo;
    private final StoreBookRepo storeBookRepo;
    private final OrderRepo orderRepo;
    private final OrderItemRepo orderItemRepo;

    public InternalCheckoutController(UserRepo userRepo, AddressRepo addressRepo, StoreBookRepo storeBookRepo, OrderRepo orderRepo, OrderItemRepo orderItemRepo) {
        this.userRepo = userRepo;
        this.addressRepo = addressRepo;
        this.storeBookRepo = storeBookRepo;
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
    }

    @PostMapping("/place-order")
    @Transactional
    public Map<String, Object> placeOrder(@RequestBody Map<String, Object> request) {
        String email = string(request.get("email"));
        Integer addressId = integer(request.get("addressId"));
        String paymentMethod = string(request.get("paymentMethod"));
        List<Map<String, Object>> items = items(request.get("items"));
        if (items.isEmpty()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cart is empty");
        User user = userRepo.findByEmailIgnoreCase(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Address address = addressRepo.findById(addressId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found"));
        if (address.getUser() == null || !Objects.equals(address.getUser().getUserId(), user.getUserId())) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Address does not belong to user");

        LocalDateTime now = LocalDateTime.now();
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setOrderDate(now);
        order.setCreatedAt(now);
        order.setUpdatedAt(now);
        order.setOrderStatus("PROCESSING");
        order.setPaymentMethod(paymentMethod.isBlank() ? "COD" : paymentMethod);
        order.setTotalAmount(BigDecimal.ZERO);
        order = orderRepo.save(order);

        BigDecimal total = BigDecimal.ZERO;
        for (Map<String, Object> item : items) {
            Integer storeBookId = integer(item.get("storeBookId"));
            Integer quantity = Math.max(1, integer(item.get("quantity")));
            StoreBook sb = storeBookRepo.findById(storeBookId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Store book not found"));
            int stock = sb.getStockQuantity() == null ? 0 : sb.getStockQuantity();
            if (stock < quantity) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only " + stock + " copies available for " + (sb.getBook() == null ? "selected book" : sb.getBook().getTitle()));
            BigDecimal price = sb.getPrice() == null ? BigDecimal.ZERO : sb.getPrice();
            BigDecimal subtotal = price.multiply(BigDecimal.valueOf(quantity));
            total = total.add(subtotal);
            sb.setStockQuantity(stock - quantity);
            storeBookRepo.save(sb);

            OrderItem oi = new OrderItem();
            oi.setOrder(order);
            oi.setStoreBook(sb);
            oi.setQuantity(quantity);
            oi.setBookPrice(price);
            oi.setSubtotal(subtotal);
            orderItemRepo.save(oi);
        }
        order.setTotalAmount(total);
        order.setUpdatedAt(LocalDateTime.now());
        order = orderRepo.save(order);
        return Map.of("order", BackendMapper.order(order), "items", orderItemRepo.findByOrder_OrderId(order.getOrderId()).stream().map(BackendMapper::orderItem).toList());
    }

    @GetMapping("/orders/{email}")
    public Map<String, Object> myOrders(@PathVariable String email) {
        List<Order> orders = orderRepo.findByUser_EmailIgnoreCase(email).stream().sorted(Comparator.comparing(Order::getOrderDate, Comparator.nullsLast(Comparator.reverseOrder()))).toList();
        return Map.of("orders", orders.stream().map(order -> {
            Map<String, Object> map = new LinkedHashMap<>(BackendMapper.order(order));
            map.put("items", orderItemRepo.findByOrder_OrderId(order.getOrderId()).stream().map(BackendMapper::orderItem).toList());
            return map;
        }).toList());
    }

    private String string(Object o) { return o == null ? "" : String.valueOf(o).trim(); }
    private Integer integer(Object o) { if (o == null || String.valueOf(o).isBlank()) return 0; return Integer.valueOf(String.valueOf(o)); }
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> items(Object o) { return o instanceof List<?> list ? (List<Map<String, Object>>) list : List.of(); }
}
