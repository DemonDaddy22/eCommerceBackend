package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private UserRepository userRepository = mock(UserRepository.class);

    private static User user;

    private static Cart cart;

    private static Item item;

    @BeforeClass
    public static void initEntities() {

        user = new User();
        user.setId(0L);
        user.setUsername("test");
        user.setPassword("test123");

        item = new Item();
        item.setId(0L);
        item.setName("Round Widget");
        item.setDescription("A widget that is round");
        item.setPrice(new BigDecimal("0.99"));
    }

    @Before
    public void setup() {
        cartController = new CartController();

        TestUtils.injectObjects(cartController,"cartRepository", cartRepository);
        TestUtils.injectObjects(cartController,"itemRepository", itemRepository);
        TestUtils.injectObjects(cartController,"userRepository", userRepository);

        cart = new Cart();
        cart.setId(0L);
        cart.setUser(user);

        user.setCart(cart);
    }

    @Test
    public void addToCartHappyPath() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(java.util.Optional.ofNullable(item));

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(item.getId());
        request.setQuantity(2);
        request.setUsername(user.getUsername());

        final ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cartResponse = response.getBody();
        assertNotNull(cartResponse);
        assertEquals(user, cartResponse.getUser());
        assertEquals(item, cartResponse.getItems().get(0));
        assertEquals(item.getPrice().multiply(new BigDecimal((cartResponse.getItems().size()))), cartResponse.getTotal());
    }

    @Test
    public void removeFromCartHappyPath() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(itemRepository.findById(item.getId())).thenReturn(java.util.Optional.ofNullable(item));

        user.getCart().addItem(item);
        user.getCart().addItem(item);

        assertEquals(2, user.getCart().getItems().size());

        ModifyCartRequest request = new ModifyCartRequest();
        request.setItemId(item.getId());
        request.setQuantity(1);
        request.setUsername(user.getUsername());

        final ResponseEntity<Cart> response = cartController.removeFromcart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cartResponse = response.getBody();
        assertNotNull(cartResponse);
        assertEquals(user, cartResponse.getUser());
        assertEquals(1, cartResponse.getItems().size());
        assertEquals(item, cartResponse.getItems().get(0));
        assertEquals(item.getPrice().multiply(new BigDecimal((cartResponse.getItems().size()))), cartResponse.getTotal());
    }
}
