package com.example.eventmanager.service;


import com.example.eventmanager.dao.ItemRepository;
import com.example.eventmanager.dao.WishListRepository;
import com.example.eventmanager.domain.WishList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WishListService {
    private final ItemRepository itemRepository;
    private final WishListRepository wishListRepository;

    private final Logger logger = LogManager.getLogger(ItemService.class);

    @Autowired
    public WishListService(ItemRepository itemRepository, WishListRepository wishListRepository){
        this.itemRepository = itemRepository;
        this.wishListRepository = wishListRepository;
    }

    public WishList getWishListByUser(Long userId) {
        return wishListRepository.getWishListByUser(userId);
    }
}
