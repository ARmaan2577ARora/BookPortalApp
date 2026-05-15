package com.example.bookPortal.repository;

import com.example.bookPortal.entity.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderItemRepoTest {

    @Autowired
    private OrderItemRepo orderItemRepo;

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private StoreBookRepo storeBookRepo;

    @Autowired
    private StoreRepo storeRepo;

    @Autowired
    private BookRepo bookRepo;

    @Autowired
    private PublisherRepo publisherRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private RolesRepo rolesRepo;

    private Roles getOrCreateRole(String roleName) {
        return rolesRepo.findByRoleNameIgnoreCase(roleName)
                .orElseGet(() -> rolesRepo.save(RepoTestHelper.role(roleName)));
    }

    private TestData createTestData() {
        String suffix = RepoTestHelper.suffix();

        Roles customer = getOrCreateRole("CUSTOMER");

        User user = userRepo.save(
                RepoTestHelper.user(suffix, customer, true)
        );

        Address address = addressRepo.save(
                RepoTestHelper.address(user, suffix, true)
        );

        Publisher publisher = publisherRepo.save(
                RepoTestHelper.publisher(suffix)
        );

        Book book = bookRepo.save(
                RepoTestHelper.book(suffix, publisher)
        );

        Store store = storeRepo.save(
                RepoTestHelper.store(suffix)
        );

        StoreBook storeBook = storeBookRepo.save(
                RepoTestHelper.storeBook(suffix, store, book)
        );

        Order order = orderRepo.save(
                RepoTestHelper.order(user, address, suffix)
        );

        OrderItem orderItem = orderItemRepo.save(
                RepoTestHelper.orderItem(order, storeBook)
        );

        return new TestData(user, book, store, storeBook, order, orderItem, suffix);
    }

    @Test
    void findByOrder_OrderId_WhenOrderExists_ReturnsOrderItems() {
        TestData data = createTestData();

        assertThat(orderItemRepo.findByOrder_OrderId(data.order.getOrderId()))
                .extracting(OrderItem::getOrderItemId)
                .contains(data.orderItem.getOrderItemId());
    }

    @Test
    void findByOrder_OrderId_WhenOrderDoesNotExist_ReturnsEmptyList() {
        assertThat(orderItemRepo.findByOrder_OrderId(Integer.MAX_VALUE))
                .isEmpty();
    }

    @Test
    void findByStoreBook_StoreBookId_WhenStoreBookExists_ReturnsOrderItems() {
        TestData data = createTestData();

        assertThat(orderItemRepo.findByStoreBook_StoreBookId(data.storeBook.getStoreBookId()))
                .extracting(OrderItem::getOrderItemId)
                .contains(data.orderItem.getOrderItemId());
    }

    @Test
    void findByStoreBook_StoreBookId_WhenStoreBookDoesNotExist_ReturnsEmptyList() {
        assertThat(orderItemRepo.findByStoreBook_StoreBookId(Integer.MAX_VALUE))
                .isEmpty();
    }

    @Test
    void findByStoreBook_Book_BookId_WhenBookExists_ReturnsOrderItems() {
        TestData data = createTestData();

        assertThat(orderItemRepo.findByStoreBook_Book_BookId(data.book.getBookId()))
                .extracting(OrderItem::getOrderItemId)
                .contains(data.orderItem.getOrderItemId());
    }

    @Test
    void findByStoreBook_Book_BookId_WhenBookDoesNotExist_ReturnsEmptyList() {
        assertThat(orderItemRepo.findByStoreBook_Book_BookId(Integer.MAX_VALUE))
                .isEmpty();
    }

    @Test
    void findByStoreBook_Store_StoreId_WhenStoreExists_ReturnsOrderItems() {
        TestData data = createTestData();

        assertThat(orderItemRepo.findByStoreBook_Store_StoreId(data.store.getStoreId()))
                .extracting(OrderItem::getOrderItemId)
                .contains(data.orderItem.getOrderItemId());
    }

    @Test
    void findByStoreBook_Store_StoreId_WhenStoreDoesNotExist_ReturnsEmptyList() {
        assertThat(orderItemRepo.findByStoreBook_Store_StoreId(Integer.MAX_VALUE))
                .isEmpty();
    }

    @Test
    void findByQuantityGreaterThan_WhenQuantityIsGreater_ReturnsOrderItems() {
        TestData data = createTestData();

        assertThat(orderItemRepo.findByQuantityGreaterThan(1))
                .extracting(OrderItem::getOrderItemId)
                .contains(data.orderItem.getOrderItemId());
    }

    @Test
    void findByQuantityGreaterThan_WhenQuantityIsTooHigh_ReturnsEmptyList() {
        assertThat(orderItemRepo.findByQuantityGreaterThan(Integer.MAX_VALUE - 1))
                .isEmpty();
    }

    @Test
    void findByBookPriceBetween_WhenPriceIsInRange_ReturnsOrderItems() {
        TestData data = createTestData();

        assertThat(orderItemRepo.findByBookPriceBetween(
                new BigDecimal("400.00"),
                new BigDecimal("600.00")
        ))
                .extracting(OrderItem::getOrderItemId)
                .contains(data.orderItem.getOrderItemId());
    }

    @Test
    void findByBookPriceBetween_WhenPriceIsOutOfRange_ReturnsEmptyList() {
        assertThat(orderItemRepo.findByBookPriceBetween(
                new BigDecimal("-1000.00"),
                new BigDecimal("-1.00")
        ))
                .isEmpty();
    }

    @Test
    void findBySubtotalBetween_WhenSubtotalIsInRange_ReturnsOrderItems() {
        TestData data = createTestData();

        assertThat(orderItemRepo.findBySubtotalBetween(
                new BigDecimal("900.00"),
                new BigDecimal("1100.00")
        ))
                .extracting(OrderItem::getOrderItemId)
                .contains(data.orderItem.getOrderItemId());
    }

    @Test
    void findBySubtotalBetween_WhenSubtotalIsOutOfRange_ReturnsEmptyList() {
        assertThat(orderItemRepo.findBySubtotalBetween(
                new BigDecimal("-1000.00"),
                new BigDecimal("-1.00")
        ))
                .isEmpty();
    }

    @Test
    void findByStoreBook_Book_TitleContainingIgnoreCase_WhenTitleMatches_ReturnsOrderItems() {
        TestData data = createTestData();

        assertThat(orderItemRepo.findByStoreBook_Book_TitleContainingIgnoreCase(data.suffix.toLowerCase()))
                .extracting(OrderItem::getOrderItemId)
                .contains(data.orderItem.getOrderItemId());
    }

    @Test
    void findByStoreBook_Book_TitleContainingIgnoreCase_WhenTitleDoesNotMatch_ReturnsEmptyList() {
        String wrongTitle = "NO_BOOK_TITLE_" + RepoTestHelper.suffix();

        assertThat(orderItemRepo.findByStoreBook_Book_TitleContainingIgnoreCase(wrongTitle))
                .isEmpty();
    }

    @Test
    void findByOrder_User_UserId_WhenUserExists_ReturnsOrderItems() {
        TestData data = createTestData();

        assertThat(orderItemRepo.findByOrder_User_UserId(data.user.getUserId()))
                .extracting(OrderItem::getOrderItemId)
                .contains(data.orderItem.getOrderItemId());
    }

    @Test
    void findByOrder_User_UserId_WhenUserDoesNotExist_ReturnsEmptyList() {
        assertThat(orderItemRepo.findByOrder_User_UserId(Integer.MAX_VALUE))
                .isEmpty();
    }

    @Test
    void findByOrder_OrderStatusIgnoreCase_WhenStatusMatches_ReturnsOrderItems() {
        TestData data = createTestData();

        assertThat(orderItemRepo.findByOrder_OrderStatusIgnoreCase(data.order.getOrderStatus().toLowerCase()))
                .extracting(OrderItem::getOrderItemId)
                .contains(data.orderItem.getOrderItemId());
    }

    @Test
    void findByOrder_OrderStatusIgnoreCase_WhenStatusDoesNotMatch_ReturnsEmptyList() {
        String wrongStatus = "NO_STATUS_" + RepoTestHelper.suffix();

        assertThat(orderItemRepo.findByOrder_OrderStatusIgnoreCase(wrongStatus))
                .isEmpty();
    }

    private static class TestData {

        private final User user;
        private final Book book;
        private final Store store;
        private final StoreBook storeBook;
        private final Order order;
        private final OrderItem orderItem;
        private final String suffix;

        private TestData(User user,
                         Book book,
                         Store store,
                         StoreBook storeBook,
                         Order order,
                         OrderItem orderItem,
                         String suffix) {
            this.user = user;
            this.book = book;
            this.store = store;
            this.storeBook = storeBook;
            this.order = order;
            this.orderItem = orderItem;
            this.suffix = suffix;
        }
    }
}