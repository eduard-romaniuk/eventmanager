package com.example.eventmanager.service;

import com.example.eventmanager.dao.ItemRepository;
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
    private final Logger logger = LogManager.getLogger(ItemService.class);

    @Autowired
    public ItemService(ItemRepository itemRepository, TagRepository tagRepository){
        this.itemRepository = itemRepository;
        this.tagRepository = tagRepository;
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


}
