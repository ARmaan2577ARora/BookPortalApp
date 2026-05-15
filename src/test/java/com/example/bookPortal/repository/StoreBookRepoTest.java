package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Book;
import com.example.bookPortal.entity.Publisher;
import com.example.bookPortal.entity.Store;
import com.example.bookPortal.entity.StoreBook;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class StoreBookRepoTest {

    @Autowired
    private StoreBookRepo storeBookRepo;

    @Autowired
    private StoreRepo storeRepo;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private PublisherRepo publisherRepo;

    private Store store;
    private Book book;
    private StoreBook storeBook;
    private String suffix;

    @BeforeEach
    void setUp() {
        suffix = RepoTestHelper.suffix();

        Publisher publisher = publisherRepo.save(
                RepoTestHelper.publisher(suffix)
        );

        book = bookRepo.save(
                RepoTestHelper.book(suffix, publisher)
        );

        store = storeRepo.save(
                RepoTestHelper.store(suffix)
        );

        storeBook = storeBookRepo.save(
                RepoTestHelper.storeBook(suffix, store, book)
        );
    }

    @Test
    void shouldFindByStoreId() {
        assertThat(storeBookRepo.findByStore_StoreId(store.getStoreId()))
                .extracting(StoreBook::getStoreBookId)
                .contains(storeBook.getStoreBookId());
    }

    @Test
    void shouldFindByBookId() {
        assertThat(storeBookRepo.findByBook_BookId(book.getBookId()))
                .extracting(StoreBook::getStoreBookId)
                .contains(storeBook.getStoreBookId());
    }

    @Test
    void shouldFindByStoreAndBook() {
        assertThat(storeBookRepo.findByStore_StoreIdAndBook_BookId(
                store.getStoreId(),
                book.getBookId()
        )).isPresent();
    }

    @Test
    void shouldCheckExistenceByStoreAndBook() {
        assertThat(storeBookRepo.existsByStore_StoreIdAndBook_BookId(
                store.getStoreId(),
                book.getBookId()
        )).isTrue();
    }

    @Test
    void shouldFindByPriceBetween() {
        assertThat(storeBookRepo.findByPriceBetween(
                new BigDecimal("400.00"),
                new BigDecimal("600.00")
        ))
                .extracting(StoreBook::getStoreBookId)
                .contains(storeBook.getStoreBookId());
    }

    @Test
    void shouldFindByStockQuantityGreaterThan() {
        assertThat(storeBookRepo.findByStockQuantityGreaterThan(5))
                .extracting(StoreBook::getStoreBookId)
                .contains(storeBook.getStoreBookId());
    }

    @Test
    void shouldFindByStockQuantityLessThanEqual() {
        assertThat(storeBookRepo.findByStockQuantityLessThanEqual(10))
                .extracting(StoreBook::getStoreBookId)
                .contains(storeBook.getStoreBookId());
    }

    @Test
    void shouldFindByDeliveryDaysLessThanEqual() {
        assertThat(storeBookRepo.findByDeliveryDaysLessThanEqual(3))
                .extracting(StoreBook::getStoreBookId)
                .contains(storeBook.getStoreBookId());
    }

    @Test
    void shouldFindByBookTitleContainingIgnoreCase() {
        assertThat(storeBookRepo.findByBook_TitleContainingIgnoreCase(suffix))
                .extracting(StoreBook::getStoreBookId)
                .contains(storeBook.getStoreBookId());
    }

    @Test
    void shouldFindByBookIsbn() {
        assertThat(storeBookRepo.findByBook_Isbn(book.getIsbn()))
                .extracting(StoreBook::getStoreBookId)
                .contains(storeBook.getStoreBookId());
    }

    @Test
    void shouldFindByStoreCityIgnoreCase() {
        assertThat(storeBookRepo.findByStore_CityIgnoreCase(store.getCity().toLowerCase()))
                .extracting(StoreBook::getStoreBookId)
                .contains(storeBook.getStoreBookId());
    }

    @Test
    void shouldFindByBookIdAndStockQuantityGreaterThan() {
        assertThat(storeBookRepo.findByBook_BookIdAndStockQuantityGreaterThan(
                book.getBookId(),
                5
        ))
                .extracting(StoreBook::getStoreBookId)
                .contains(storeBook.getStoreBookId());
    }
}