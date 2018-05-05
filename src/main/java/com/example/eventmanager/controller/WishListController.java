package com.example.eventmanager.controller;

import com.example.eventmanager.domain.Item;
import com.example.eventmanager.domain.WishList;
import com.example.eventmanager.service.ItemService;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.logging.Logger;

@RestController
@RequestMapping(value = "/wishlist")
public class WishListController {
    private final ItemService itemService;
    private final org.apache.logging.log4j.Logger logger = LogManager.getLogger(ItemController.class);

    @Autowired
    public WishListController(ItemService itemService) {
        this.itemService = itemService;
        logger.info("Class initialized");
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Item>> createEvent(@PathVariable("id") Long id) {
        logger.info("GET /" + id +" get items for wish list");

        //TODO: CHANGE!!!
        WishList wishList = new WishList();

        wishList.setId(id);

        return new ResponseEntity<>(itemService.getItemsForWishList(wishList), HttpStatus.OK);
    }
}
