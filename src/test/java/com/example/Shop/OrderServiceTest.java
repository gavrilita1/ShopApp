package com.example.Shop;

import com.example.Shop.dto.OrderDTO;
import com.example.Shop.dto.OrderRequestDTO;
import com.example.Shop.entity.Order;
import com.example.Shop.entity.Product;
import com.example.Shop.entity.User;
import com.example.Shop.repository.OrderRepository;
import com.example.Shop.repository.ProductRepository;
import com.example.Shop.repository.UserRepository;
import com.example.Shop.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * UNIT TEST for OrderService
 *
 * IMPORTANT:
 * This test DOES NOT start Spring Boot.
 * We test ONLY business logic.
 *
 * All repositories are mocked using Mockito.
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    /**
     * Mock repositories.
     * Mockito creates fake implementations.
     */
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ProductRepository productRepository;

    /**
     * Class under test.
     * Mockito injects mocks automatically.
     */
    @InjectMocks
    private OrderService orderService;

    /**
     * TEST CASE:
     * Order is successfully created.
     *
     * We verify:
     *  - user is loaded
     *  - products are loaded
     *  - total is calculated
     *  - order is saved
     *  - DTO is returned correctly
     */
    @Test
    void shouldCreateOrderSuccessfully() {

        // =========================
        // GIVEN (test setup)
        // =========================

        // Fake user existing in database
        User user = new User(1L, "John", "john@mail.com", null);

        // Fake products
        Product product1 =
                new Product(1L, "Laptop", "Gaming laptop", 2000.0, 5, null);

        Product product2 =
                new Product(2L, "Mouse", "Wireless mouse", 100.0, 10, null);

        // Request received from controller
        OrderRequestDTO request =
                new OrderRequestDTO(1L, List.of(1L, 2L));

        // Define mock behaviour
        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(productRepository.findAllById(List.of(1L, 2L)))
                .thenReturn(List.of(product1, product2));

        /**
         * When save() is called,
         * return the same order but simulate DB generated ID.
         */
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order order = invocation.getArgument(0);
                    order.setId(10L); // simulate DB auto-id
                    order.setCreatedAt(new Date());
                    return order;
                });

        // =========================
        // WHEN (execute logic)
        // =========================
        OrderDTO result = orderService.createOrder(request);

        // =========================
        // THEN (assert results)
        // =========================

        // DTO should not be null
        assertNotNull(result);

        // Verify calculated total price
        assertEquals(2100.0, result.total());

        // Verify username mapping
        assertEquals("John", result.customerName());

        // Verify products mapped as names
        assertEquals(2, result.productNames().size());
        assertTrue(result.productNames().contains("Laptop"));
        assertTrue(result.productNames().contains("Mouse"));

        // Verify repository interaction
        verify(orderRepository).save(any(Order.class));
    }

    /**
     * TEST CASE:
     * User does NOT exist.
     *
     * Expected:
     *  - RuntimeException thrown
     *  - Order is NOT saved
     */
    @Test
    void shouldThrowExceptionWhenUserNotFound() {

        // GIVEN
        OrderRequestDTO request =
                new OrderRequestDTO(99L, List.of(1L));

        when(userRepository.findById(99L))
                .thenReturn(Optional.empty());

        // WHEN + THEN
        assertThrows(RuntimeException.class,
                () -> orderService.createOrder(request));

        // ensure save was never called
        verify(orderRepository, never()).save(any());
    }

    /**
     * TEST CASE:
     * Verify getAllOrders mapping logic.
     *
     * We test entity -> DTO transformation.
     */
    @Test
    void shouldReturnAllOrders() {

        // GIVEN
        User user = new User(1L, "Alice", "alice@mail.com", null);

        Product product =
                new Product(1L, "Keyboard", "Mechanical", 300.0, 3, null);

        Order order = new Order(
                1L,
                user,
                List.of(product),
                300.0,
                new Date()
        );

        when(orderRepository.findAll())
                .thenReturn(List.of(order));

        // WHEN
        List<OrderDTO> result = orderService.getAllOrders();

        // THEN
        assertEquals(1, result.size());

        OrderDTO dto = result.get(0);

        assertEquals("Alice", dto.customerName());
        assertEquals("Keyboard", dto.productNames().get(0));
        assertEquals(300.0, dto.total());

        verify(orderRepository).findAll();
    }
}