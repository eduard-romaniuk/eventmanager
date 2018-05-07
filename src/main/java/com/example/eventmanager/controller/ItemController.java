package com.example.eventmanager.controller;

import com.example.eventmanager.domain.Item;
import com.example.eventmanager.domain.Tag;
import com.example.eventmanager.domain.WishList;
import com.example.eventmanager.domain.transfer.validation.ItemValidation;
import com.example.eventmanager.service.ItemService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = "/item")
public class ItemController {
    private final ItemService itemService;
    private final Logger logger = LogManager.getLogger(ItemController.class);

    @Autowired
    public ItemController(ItemService itemService) {
        this.itemService = itemService;
        logger.info("Class initialized");
    }

    @RequestMapping(value = "/", method = RequestMethod.POST)
    public void createItem (@Validated(ItemValidation.New.class) @RequestBody Item item, UriComponentsBuilder ucBuilder) {
        logger.info("POST / create new item");

        itemService.saveItem(item);
        //HttpHeaders headers = new HttpHeaders();
        //headers.add("id", "1" + event.getId());
        //headers.setLocation(ucBuilder.path("/event/{id}").buildAndExpand(event.getId()).toUri());
    }

    @RequestMapping(value = "/like/{id}", method = RequestMethod.POST)
    public void like (@PathVariable("id") Long itemId, @RequestBody Long userId) {
        logger.info("POST /like/" + itemId);

        itemService.likeItem(userId, itemId);

    }

    @RequestMapping(value = "/dislike/{id}", method = RequestMethod.POST)
    public void dislike (@PathVariable("id") Long itemId, @RequestBody Long userId) {
        logger.info("POST /dislike/" + itemId);

        itemService.dislikeItem(userId, itemId);
    }

    @RequestMapping(value = "/likes/{userId}/{itemId}", method = RequestMethod.GET)
    public ResponseEntity<Boolean> hasLike(@PathVariable("userId") Long userId, @PathVariable("itemId") Long itemId) {
        logger.info("GET /" + userId + "/" + itemId);

        return new ResponseEntity<Boolean>(itemService.isUserLikesItem(userId, itemId), HttpStatus.OK);
    }


}
