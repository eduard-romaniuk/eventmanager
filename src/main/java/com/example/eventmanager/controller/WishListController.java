package com.example.eventmanager.controller;

import com.example.eventmanager.domain.Item;
import com.example.eventmanager.domain.WishList;
import com.example.eventmanager.domain.transfer.view.ItemView;
import com.example.eventmanager.service.ItemService;
import com.example.eventmanager.service.WishListService;
import com.fasterxml.jackson.annotation.JsonView;
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
    private final WishListService wishListService;
    private final org.apache.logging.log4j.Logger logger = LogManager.getLogger(ItemController.class);

    @Autowired
    public WishListController(ItemService itemService, WishListService wishListService) {
        this.itemService = itemService;
        this.wishListService = wishListService;
        logger.info("Class initialized");
    }

    @JsonView(ItemView.ShortView.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.GET)
    public ResponseEntity<List<Item>> getListOfItems(@PathVariable("id") Long wishListId) {
        logger.info("GET /" + wishListId );

        return new ResponseEntity<>(itemService.getItemsForWishList(wishListId), HttpStatus.OK);
    }

//    @JsonView(ItemView.ShortView.class)
    @RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
    public ResponseEntity<WishList> getWishListByUser(@PathVariable("id") Long userId) {
        logger.info("GET /user/" + userId );

        return new ResponseEntity<>(wishListService.getWishListByUser(userId), HttpStatus.OK);
    }
}
