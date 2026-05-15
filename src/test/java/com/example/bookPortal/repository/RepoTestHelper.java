package com.example.bookPortal.repository;

import com.example.bookPortal.entity.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

class RepoTestHelper {

    static String suffix() {
        return UUID.randomUUID().toString().substring(0, 8);
    }

    static String phone() {
        long number = Math.abs(UUID.randomUUID().getMostSignificantBits());
        return "9" + String.format("%09d", number % 1_000_000_000L);
    }

    static String website(String suffix) {
        return "https://test" + suffix + ".com";
    }

    static Roles role(String roleName) {
        Roles role = new Roles();
        role.setRoleName(roleName);
        return role;
    }

    static User user(String suffix, Roles role, Boolean active) {
        User user = new User();
        user.setFullName("User_" + suffix);
        user.setEmail("user_" + suffix + "@test.com");
        user.setPasswordHash("password");
        user.setPhone(phone());
        user.setCreatedAt(LocalDateTime.now());
        user.setLastLogin(LocalDateTime.now());
        user.setIsActive(active);
        user.setRole(role);
        return user;
    }

    static Address address(User user, String suffix, Boolean isDefault) {
        Address address = new Address();
        address.setHouseAddress("House_" + suffix);
        address.setCity("City_" + suffix);
        address.setState("State_" + suffix);
        address.setZipcode("ZIP" + suffix);
        address.setCountry("Country_" + suffix);
        address.setIsDefault(isDefault);
        address.setUser(user);
        return address;
    }

    static Author author(String suffix) {
        Author author = new Author();
        author.setFirstName("First_" + suffix);
        author.setLastName("Last_" + suffix);
        author.setBio("Bio_" + suffix);
        author.setPhone(phone());
        author.setCountry("Country_" + suffix);
        return author;
    }

    static Publisher publisher(String suffix) {
        Publisher publisher = new Publisher();
        publisher.setPublisherName("Publisher_" + suffix);
        publisher.setCity("City_" + suffix);
        publisher.setState("State_" + suffix);
        publisher.setCountry("Country_" + suffix);
        publisher.setWebsite(website("pub" + suffix));
        return publisher;
    }

    static Book book(String suffix, Publisher publisher) {
        Book book = new Book();
        book.setTitle("Book_" + suffix);
        book.setIsbn("ISBN" + suffix);
        book.setDescription("Description_" + suffix);
        book.setPublishedDate(LocalDate.now());
        book.setLanguage("English_" + suffix);
        book.setPages(250);
        book.setCoverImage("cover_" + suffix + ".jpg");
        book.setCreatedAt(LocalDateTime.now());
        book.setPublisher(publisher);
        return book;
    }

    static Store store(String suffix) {
        Store store = new Store();
        store.setStoreName("Store_" + suffix);
        store.setCity("City_" + suffix);
        store.setState("State_" + suffix);
        store.setCountry("Country_" + suffix);
        store.setWebsite(website("store" + suffix));
        store.setRating(new BigDecimal("4.50"));
        return store;
    }

    static BookAuthor bookAuthor(Book book, Author author, Integer authorOrder) {
        BookAuthor bookAuthor = new BookAuthor();
        bookAuthor.setBook(book);
        bookAuthor.setAuthor(author);
        bookAuthor.setAuthorOrder(authorOrder);
        return bookAuthor;
    }

    static StoreBook storeBook(String suffix, Store store, Book book) {
        StoreBook storeBook = new StoreBook();
        storeBook.setStore(store);
        storeBook.setBook(book);
        storeBook.setPrice(new BigDecimal("500.00"));
        storeBook.setStockQuantity(10);
        storeBook.setDeliveryDays(3);
        storeBook.setStoreBookUrl(website("storebook" + suffix));
        storeBook.setCreatedAt(LocalDateTime.now());
        return storeBook;
    }

    static Order order(User user, Address address, String suffix) {
        Order order = new Order();
        order.setUser(user);
        order.setAddress(address);
        order.setOrderDate(LocalDateTime.now());
        order.setPaymentMethod("UPI_" + suffix);
        order.setOrderStatus("PLACED_" + suffix);
        order.setTotalAmount(new BigDecimal("1000.00"));
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        return order;
    }

    static OrderItem orderItem(Order order, StoreBook storeBook) {
        OrderItem orderItem = new OrderItem();
        orderItem.setOrder(order);
        orderItem.setStoreBook(storeBook);
        orderItem.setQuantity(2);
        orderItem.setBookPrice(new BigDecimal("500.00"));
        orderItem.setSubtotal(new BigDecimal("1000.00"));
        return orderItem;
    }
}