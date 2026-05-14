package com.example.bookPortal.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "store_books")
public class StoreBook {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer storeBookId;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "stock_quantity")
    private Integer stockQuantity;

    @Column(name = "delivery_days")
    private Integer deliveryDays;

    @Column(name = "store_book_url", length = 255)
    private String storeBookUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "store_id")
    private Store store;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    public Integer getStoreBookId() {
        return storeBookId;
    }

    public void setStoreBookId(Integer storeBookId) {
        this.storeBookId = storeBookId;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public Integer getDeliveryDays() {
        return deliveryDays;
    }

    public void setDeliveryDays(Integer deliveryDays) {
        this.deliveryDays = deliveryDays;
    }

    public String getStoreBookUrl() {
        return storeBookUrl;
    }

    public void setStoreBookUrl(String storeBookUrl) {
        this.storeBookUrl = storeBookUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }
}

