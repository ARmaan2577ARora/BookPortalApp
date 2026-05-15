package com.example.bookPortal.repository;

import com.example.bookPortal.entity.Address;
import com.example.bookPortal.entity.Order;
import com.example.bookPortal.entity.Roles;
import com.example.bookPortal.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class OrderRepoTest {

    @Autowired
    private OrderRepo orderRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private AddressRepo addressRepo;

    @Autowired
    private RolesRepo rolesRepo;

    private Roles customer;
    private User user;
    private Address address;
    private Order oldOrder;
    private Order latestOrder;

    private String suffix;

    private Roles getOrCreateRole(String roleName) {
        return rolesRepo.findByRoleNameIgnoreCase(roleName)
                .orElseGet(() -> rolesRepo.save(RepoTestHelper.role(roleName)));
    }

    @BeforeEach
    void setUp() {
        suffix = RepoTestHelper.suffix();

        customer = getOrCreateRole("CUSTOMER");

        user = userRepo.save(
                RepoTestHelper.user(suffix, customer, true)
        );

        address = addressRepo.save(
                RepoTestHelper.address(user, suffix, true)
        );

        oldOrder = RepoTestHelper.order(user, address, suffix);
        oldOrder.setOrderDate(LocalDateTime.now().minusDays(1));
        oldOrder = orderRepo.save(oldOrder);

        latestOrder = RepoTestHelper.order(user, address, suffix);
        latestOrder.setOrderDate(LocalDateTime.now());
        latestOrder = orderRepo.save(latestOrder);
    }

    @Test
    void shouldFindOrdersByUserId() {
        assertThat(orderRepo.findByUser_UserId(user.getUserId()))
                .extracting(Order::getOrderId)
                .contains(oldOrder.getOrderId(), latestOrder.getOrderId());
    }

    @Test
    void shouldFindOrdersByAddressId() {
        assertThat(orderRepo.findByAddress_AddressId(address.getAddressId()))
                .extracting(Order::getOrderId)
                .contains(oldOrder.getOrderId(), latestOrder.getOrderId());
    }

    @Test
    void shouldFindOrdersByOrderStatusIgnoreCase() {
        assertThat(orderRepo.findByOrderStatusIgnoreCase(
                latestOrder.getOrderStatus().toLowerCase()
        ))
                .extracting(Order::getOrderId)
                .contains(latestOrder.getOrderId());
    }

    @Test
    void shouldFindOrdersByPaymentMethodIgnoreCase() {
        assertThat(orderRepo.findByPaymentMethodIgnoreCase(
                latestOrder.getPaymentMethod().toLowerCase()
        ))
                .extracting(Order::getOrderId)
                .contains(latestOrder.getOrderId());
    }

    @Test
    void shouldFindOrdersByOrderDateBetween() {
        assertThat(orderRepo.findByOrderDateBetween(
                LocalDateTime.now().minusDays(2),
                LocalDateTime.now().plusDays(1)
        ))
                .extracting(Order::getOrderId)
                .contains(oldOrder.getOrderId(), latestOrder.getOrderId());
    }

    @Test
    void shouldFindOrdersByTotalAmountBetween() {
        assertThat(orderRepo.findByTotalAmountBetween(
                new BigDecimal("900.00"),
                new BigDecimal("1100.00")
        ))
                .extracting(Order::getOrderId)
                .contains(oldOrder.getOrderId(), latestOrder.getOrderId());
    }

    @Test
    void shouldFindOrdersByUserEmailIgnoreCase() {
        assertThat(orderRepo.findByUser_EmailIgnoreCase(
                user.getEmail().toUpperCase()
        ))
                .extracting(Order::getOrderId)
                .contains(oldOrder.getOrderId(), latestOrder.getOrderId());
    }

    @Test
    void shouldFindOrdersByUserFullNameContainingIgnoreCase() {
        assertThat(orderRepo.findByUser_FullNameContainingIgnoreCase(suffix))
                .extracting(Order::getOrderId)
                .contains(oldOrder.getOrderId(), latestOrder.getOrderId());
    }

    @Test
    void shouldReturnLatestOrderForUser() {
        assertThat(orderRepo.findTop10ByUser_UserIdOrderByOrderDateDesc(user.getUserId()))
                .first()
                .extracting(Order::getOrderId)
                .isEqualTo(latestOrder.getOrderId());
    }

    @Test
    void shouldFindOrdersByUserIdAndStatusIgnoreCase() {
        assertThat(orderRepo.findByUser_UserIdAndOrderStatusIgnoreCase(
                user.getUserId(),
                latestOrder.getOrderStatus().toLowerCase()
        ))
                .extracting(Order::getOrderId)
                .contains(latestOrder.getOrderId());
    }
}