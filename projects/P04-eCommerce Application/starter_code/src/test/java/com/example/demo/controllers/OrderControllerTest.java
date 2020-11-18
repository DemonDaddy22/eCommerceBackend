package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    private static User user;

    private static Cart cart;

    private static Item item;

    @BeforeClass
    public static void init() {

        user = new User();
        user.setId(0L);
        user.setUsername("test");
        user.setPassword("test123");

        item = new Item();
        item.setId(0L);
        item.setName("Round Widget");
        item.setPrice(new BigDecimal("2.99"));
        item.setDescription("A widget that is round");
    }

    @Before
    public void setup() {

        orderController = new OrderController();

        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
        TestUtils.injectObjects(orderController, "userRepository", userRepository);

        cart = new Cart();
        cart.setId(0L);
        cart.setUser(user);
        cart.addItem(item);
        user.setCart(cart);
    }

    @Test
    public void submitHappyPath() {

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        final ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        UserOrder userOrder = response.getBody();

        assertNotNull(userOrder);
        assertEquals(cart.getUser().getUsername(), userOrder.getUser().getUsername());
        assertEquals(cart.getItems(), userOrder.getItems());
        assertEquals(cart.getTotal(), userOrder.getTotal());
    }

    @Test
    public void historyHappyPath() {

        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);

        final ResponseEntity<UserOrder> submitResponse = orderController.submit(user.getUsername());

        assertNotNull(submitResponse);
        assertEquals(200, submitResponse.getStatusCodeValue());

        UserOrder order = submitResponse.getBody();
        assertNotNull(order);

        final ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser(user.getUsername());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<UserOrder> orders = response.getBody();

        assertNotNull(orders);
        assertEquals(order.getItems(), orders.get(0).getItems());
        assertEquals(order.getTotal(), orders.get(0).getTotal());
    }
}
