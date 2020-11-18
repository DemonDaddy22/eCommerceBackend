package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;

    private static Item item1;

    private static List<Item> items;

    private final ItemRepository itemRepository = mock(ItemRepository.class);

    @BeforeClass
    public static void initEntities() {
        item1 = new Item();
        item1.setId(1L);
        item1.setName("Round Widget");

        items = new ArrayList<Item>();
        items.add(item1);
    }

    @Before
    public void setup() {

        itemController = new ItemController();

        TestUtils.injectObjects(itemController,"itemRepository", itemRepository);
    }

    @Test
    public void getItemsHappyPath() {

        when(itemRepository.findAll()).thenReturn(items);

        final ResponseEntity<List<Item>> response = itemController.getItems();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> responseItems = response.getBody();
        assertNotNull(responseItems);
        assertEquals(1, responseItems.size());
        assertEquals(item1.getId(), responseItems.get(0).getId());
        assertEquals(item1.getName(), responseItems.get(0).getName());
    }

    @Test
    public void getItemByIdHappyPath() {
        when(itemRepository.findById(item1.getId())).thenReturn(Optional.ofNullable(item1));

        final ResponseEntity<Item> response = itemController.getItemById(item1.getId());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Item item = response.getBody();
        assertNotNull(item);
        assertEquals(item1, item);
    }

    @Test
    public void getItemsByName() {
        when(itemRepository.findByName(item1.getName())).thenReturn(items);

        final ResponseEntity<List<Item>> response = itemController.getItemsByName(item1.getName());

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> responseItems = response.getBody();
        assertNotNull(responseItems);
        assertEquals(item1.getName(), responseItems.get(0).getName());
    }
}
