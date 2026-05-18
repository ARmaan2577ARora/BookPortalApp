package com.example.bookPortal.service;

import com.example.bookPortal.entity.StoreBook;
import com.example.bookPortal.exception.BadRequestException;
import com.example.bookPortal.exception.DuplicateResourceException;
import com.example.bookPortal.repository.StoreBookRepo;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.HandleBeforeSave;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RepositoryEventHandler(StoreBook.class)
public class StoreBookRepositoryEventHandler {

    private final StoreBookRepo storeBookRepo;

    public StoreBookRepositoryEventHandler(StoreBookRepo storeBookRepo) {
        this.storeBookRepo = storeBookRepo;
    }

    @HandleBeforeCreate
    public void handleBeforeCreate(StoreBook storeBook) {
        validateStoreBook(storeBook);
        checkDuplicateStoreBook(storeBook);

        if (storeBook.getCreatedAt() == null) {
            storeBook.setCreatedAt(LocalDateTime.now());
        }
    }

    @HandleBeforeSave
    public void handleBeforeSave(StoreBook storeBook) {
        validateStoreBook(storeBook);
    }

    private void validateStoreBook(StoreBook storeBook) {
        if (storeBook.getStore() == null || storeBook.getStore().getStoreId() == null) {
            throw new BadRequestException("Store is required");
        }

        if (storeBook.getBook() == null || storeBook.getBook().getBookId() == null) {
            throw new BadRequestException("Book is required");
        }

        if (storeBook.getPrice() == null || storeBook.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Price must be greater than 0");
        }

        if (storeBook.getStockQuantity() == null || storeBook.getStockQuantity() < 0) {
            throw new BadRequestException("Stock quantity cannot be negative");
        }

        if (storeBook.getDeliveryDays() == null || storeBook.getDeliveryDays() < 0) {
            throw new BadRequestException("Delivery days cannot be negative");
        }
    }

    private void checkDuplicateStoreBook(StoreBook storeBook) {
        Integer storeId = storeBook.getStore().getStoreId();
        Integer bookId = storeBook.getBook().getBookId();

        if (storeBookRepo.existsByStore_StoreIdAndBook_BookId(storeId, bookId)) {
            throw new DuplicateResourceException("This book already exists in this store");
        }
    }
}