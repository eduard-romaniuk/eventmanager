package com.example.eventmanager.controller;

import com.example.eventmanager.domain.Item;
import com.example.eventmanager.domain.Tag;
import com.example.eventmanager.domain.WishList;
import com.example.eventmanager.service.ItemService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
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
    public void createEvent(@RequestBody Item item, UriComponentsBuilder ucBuilder) {
        logger.info("POST / create new item");

        itemService.saveItem(item);
        //HttpHeaders headers = new HttpHeaders();
        //headers.add("id", "1" + event.getId());
        //headers.setLocation(ucBuilder.path("/event/{id}").buildAndExpand(event.getId()).toUri());
    }
}
