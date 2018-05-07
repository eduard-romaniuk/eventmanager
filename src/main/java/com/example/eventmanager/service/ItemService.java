package com.example.eventmanager.service;

import com.example.eventmanager.dao.ItemRepository;
import com.example.eventmanager.dao.LikeRepository;
import com.example.eventmanager.dao.TagRepository;
import com.example.eventmanager.domain.Item;
import com.example.eventmanager.domain.Tag;
import com.example.eventmanager.domain.WishList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final TagRepository tagRepository;
    private final LikeRepository likeRepository;
    private final Logger logger = LogManager.getLogger(ItemService.class);

    @Autowired
    public ItemService(ItemRepository itemRepository, TagRepository tagRepository, LikeRepository likeRepository){
        this.itemRepository = itemRepository;
        this.tagRepository = tagRepository;
        this.likeRepository = likeRepository;
    }

    public void saveItem(Item item){

        for ( Tag tag : item.getTags() ){
            Long tagId = tagRepository.findByName(tag.getName());

            if ( tagId == null){
                tagRepository.save(tag);
            } else {
                tag.setId(tagId);
            }
        }

        itemRepository.save(item);
        tagRepository.saveItemTags(item.getTags(), item.getId());
    }

    public List<Item> getItemsForWishList (Long wishListId) {
        return itemRepository.getItemsForWishList(wishListId);
    }

    public boolean isUserLikesItem ( Long userId, Long itemId ) {
        return likeRepository.isUserLikesItem(userId, itemId);
    }

    public void likeItem (Long userId, Long itemId){
        if (!likeRepository.isUserLikesItem(userId, itemId))
            likeRepository.save(userId, itemId);
        else logger.info("The user " + userId + " already likes the item " + itemId);
    }

    public void dislikeItem (Long userId, Long itemId){
        if (likeRepository.isUserLikesItem(userId, itemId))
            likeRepository.delete(userId, itemId);
        else logger.info("The user " + userId + " doesnt like the item " + itemId);
    }


}
