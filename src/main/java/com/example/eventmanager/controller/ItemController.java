package com.example.eventmanager.controller;

import com.example.eventmanager.domain.Booker;
import com.example.eventmanager.domain.Item;
import com.example.eventmanager.domain.Tag;
import com.example.eventmanager.domain.WishList;
import com.example.eventmanager.domain.transfer.validation.ItemValidation;
import com.example.eventmanager.domain.transfer.view.ItemView;
import com.example.eventmanager.service.ItemService;
import com.fasterxml.jackson.annotation.JsonView;
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

    @JsonView(ItemView.ShortView.class)
    @RequestMapping(value = "/", method = RequestMethod.POST)
    public ResponseEntity<Item> createItem (@Validated(ItemValidation.New.class) @RequestBody Item item) {
        logger.info("POST /");

        itemService.saveItem(item);

        item.setLikes(0);

        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @JsonView(ItemView.FullView.class)
    @RequestMapping(value = "/{itemId}", method = RequestMethod.GET)
    public ResponseEntity<Item> getItem(@PathVariable("itemId") Long itemId) {
        logger.info("GET /" + itemId );

        Item item = itemService.getItem(itemId);

        if (item == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }


        return new ResponseEntity<>(item, HttpStatus.OK);
    }

    @JsonView(ItemView.ShortView.class)
    @RequestMapping(value = "/{id}", method = RequestMethod.POST)
    public ResponseEntity<Item> updateItem(@PathVariable("id") Long itemId, @Validated(ItemValidation.Exist.class)@RequestBody Item newItem) {
        logger.info("POST /" + itemId);

        Item oldItem = itemService.getItem(itemId);

        if (oldItem == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        itemService.updateItem(newItem);

        return new ResponseEntity<>(newItem, HttpStatus.OK);
    }

    @JsonView(ItemView.ShortView.class)
    @RequestMapping(value = "/copy/{id}", method = RequestMethod.POST)
    public ResponseEntity<Long> copyItem (@PathVariable("id") Long itemId, @RequestBody Long wishListId) {
        logger.info("POST /copy/" + itemId);

        Long newItemId = itemService.copyItem(wishListId, itemId);

        return new ResponseEntity<>(newItemId, HttpStatus.OK);
    }

    @JsonView(ItemView.ShortView.class)
    @RequestMapping(value = "/booking", method = RequestMethod.POST)
    public ResponseEntity<Booker> booking (@RequestBody Booker booker) {
        logger.info( "POST /booking " );

        itemService.booking(booker);

        return new ResponseEntity<>(booker, HttpStatus.OK);
    }

    @JsonView(ItemView.ShortView.class)
    @RequestMapping(value = "/booking/{itemId}/{eventId}/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity<String> unbooking (@PathVariable("itemId") Long itemId, @PathVariable("eventId") Long eventId, @PathVariable("userId") Long userId) {
        logger.info("DELETE /booking");

        //TODO: if

        Booker booker = new Booker();
        booker.setItemId(itemId);
        booker.setEventId(eventId);
        booker.setUserId(userId);
        itemService.unbooking(booker);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @RequestMapping(value = "/booking", method = RequestMethod.GET)
    public ResponseEntity<Boolean> isBooked(@RequestParam Long user, @RequestParam Long event, @RequestParam Long item) {
        logger.info("GET /booking");

        Booker booker = new Booker();
        booker.setUserId(user);
        booker.setEventId(event);
        booker.setItemId(item);

        return new ResponseEntity<>(itemService.isBooked(booker), HttpStatus.OK);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    public ResponseEntity<String> deleteItem(@PathVariable("id") Long itemId) {
        logger.info("DELETE /" + itemId);

        Item item = itemService.getItem(itemId);
        if (item == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        itemService.deleteItem(item);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
