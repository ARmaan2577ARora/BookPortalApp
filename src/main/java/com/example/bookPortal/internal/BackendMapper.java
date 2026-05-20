package com.example.bookPortal.internal;

import com.example.bookPortal.entity.*;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

final class BackendMapper {
    private BackendMapper() {}

    static Map<String, Object> role(Roles role) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (role != null) {
            map.put("roleId", role.getRoleId());
            map.put("roleName", role.getRoleName());
        }
        return map;
    }

    static Map<String, Object> user(User user) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (user == null) return map;
        map.put("userId", user.getUserId());
        map.put("fullName", user.getFullName());
        map.put("email", user.getEmail());
        map.put("phone", user.getPhone());
        map.put("isActive", user.getIsActive());
        map.put("role", role(user.getRole()));
        map.put("roleName", user.getRole() == null ? "" : user.getRole().getRoleName());
        return map;
    }

    static Map<String, Object> publisher(Publisher publisher) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (publisher == null) return map;
        map.put("publisherId", publisher.getPublisherId());
        map.put("publisherName", publisher.getPublisherName());
        map.put("city", publisher.getCity());
        map.put("state", publisher.getState());
        map.put("country", publisher.getCountry());
        map.put("website", publisher.getWebsite());
        return map;
    }

    static Map<String, Object> author(Author author) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (author == null) return map;
        map.put("authorId", author.getAuthorId());
        map.put("firstName", author.getFirstName());
        map.put("lastName", author.getLastName());
        map.put("fullName", ((author.getFirstName() == null ? "" : author.getFirstName()) + " " + (author.getLastName() == null ? "" : author.getLastName())).trim());
        map.put("bio", author.getBio());
        map.put("phone", author.getPhone());
        map.put("country", author.getCountry());
        return map;
    }

    static Map<String, Object> book(Book book, List<Author> authors, BigDecimal minPrice, Integer totalStock) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (book == null) return map;
        map.put("bookId", book.getBookId());
        map.put("title", book.getTitle());
        map.put("isbn", book.getIsbn());
        map.put("description", book.getDescription());
        map.put("publishedDate", book.getPublishedDate() == null ? null : book.getPublishedDate().toString());
        map.put("language", book.getLanguage());
        map.put("pages", book.getPages());
        map.put("coverImage", book.getCoverImage());
        map.put("publisher", publisher(book.getPublisher()));
        map.put("publisherName", book.getPublisher() == null ? "" : book.getPublisher().getPublisherName());
        map.put("authors", authors == null ? List.of() : authors.stream().map(BackendMapper::author).toList());
        map.put("authorNames", authors == null ? "" : authors.stream().map(a -> ((a.getFirstName() == null ? "" : a.getFirstName()) + " " + (a.getLastName() == null ? "" : a.getLastName())).trim()).reduce((a,b) -> a + ", " + b).orElse(""));
        map.put("minPrice", minPrice);
        map.put("totalStock", totalStock == null ? 0 : totalStock);
        return map;
    }

    static Map<String, Object> store(Store store) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (store == null) return map;
        map.put("storeId", store.getStoreId());
        map.put("storeName", store.getStoreName());
        map.put("city", store.getCity());
        map.put("state", store.getState());
        map.put("country", store.getCountry());
        map.put("website", store.getWebsite());
        map.put("rating", store.getRating());
        return map;
    }

    static Map<String, Object> storeBook(StoreBook sb) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (sb == null) return map;
        map.put("storeBookId", sb.getStoreBookId());
        map.put("price", sb.getPrice());
        map.put("stockQuantity", sb.getStockQuantity());
        map.put("deliveryDays", sb.getDeliveryDays());
        map.put("storeBookUrl", sb.getStoreBookUrl());
        map.put("store", store(sb.getStore()));
        map.put("book", book(sb.getBook(), List.of(), sb.getPrice(), sb.getStockQuantity()));
        return map;
    }

    static Map<String, Object> address(Address address) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (address == null) return map;
        map.put("addressId", address.getAddressId());
        map.put("houseAddress", address.getHouseAddress());
        map.put("city", address.getCity());
        map.put("state", address.getState());
        map.put("zipcode", address.getZipcode());
        map.put("country", address.getCountry());
        map.put("isDefault", address.getIsDefault());
        return map;
    }

    static Map<String, Object> order(Order order) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (order == null) return map;
        map.put("orderId", order.getOrderId());
        map.put("orderDate", order.getOrderDate() == null ? null : order.getOrderDate().toString());
        map.put("paymentMethod", order.getPaymentMethod());
        map.put("orderStatus", order.getOrderStatus());
        map.put("totalAmount", order.getTotalAmount());
        map.put("user", user(order.getUser()));
        map.put("address", address(order.getAddress()));
        return map;
    }

    static Map<String, Object> orderItem(OrderItem item) {
        Map<String, Object> map = new LinkedHashMap<>();
        if (item == null) return map;
        map.put("orderItemId", item.getOrderItemId());
        map.put("quantity", item.getQuantity());
        map.put("bookPrice", item.getBookPrice());
        map.put("subtotal", item.getSubtotal());
        map.put("storeBook", storeBook(item.getStoreBook()));
        return map;
    }
}
