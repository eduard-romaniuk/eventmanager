package com.example.eventmanager.service;

import com.example.eventmanager.dao.*;
import com.example.eventmanager.domain.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemService {
    private final ItemRepository itemRepository;
    private final TagRepository tagRepository;
    private final LikeRepository likeRepository;
    private final BookerRepository bookerRepository;
    private final EventRepository eventRepository;


    private final Logger logger = LogManager.getLogger(ItemService.class);

    @Autowired
    public ItemService(
            ItemRepository itemRepository,
            TagRepository tagRepository,
            LikeRepository likeRepository,
            BookerRepository bookerRepository,
            EventRepository eventRepository
            ){
        this.itemRepository = itemRepository;
        this.tagRepository = tagRepository;
        this.likeRepository = likeRepository;
        this.bookerRepository = bookerRepository;
        this.eventRepository = eventRepository;
    }

    public void saveItem(Item item){

        setNewTags(item.getTags());

        itemRepository.save(item);

    }

    public Item getItem (Long itemId) {
        return itemRepository.findOne(itemId);
    }

    public void updateItem(Item item){

        setNewTags( item.getTags() );

        itemRepository.update( item );

    }

    public void deleteItem (Item item) {

        itemRepository.delete( item );

    }

    public Long copyItem ( Long toWishListId, Long itemId) {
        Long newItemId = itemRepository.copyItem (toWishListId, itemId);
        List<Tag> tags = tagRepository.getTagsForItem(itemId);

        for ( Tag tag : tags ) {
            tagRepository.addItemTag( newItemId, tag.getId());
        }

        return newItemId;
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

    private void setNewTags (List<Tag> tags) {

        for ( Tag tag : tags ){
            Long tagId = tagRepository.findByName(tag.getName());

            if ( tagId == null){
                tagRepository.save(tag);
            } else {
                tag.setId(tagId);
            }
        }

    }

    public void booking (Booker booker) {

        if ( eventRepository.isParticipant(booker.getUserId(), booker.getEventId()) ) {
            bookerRepository.save(booker);
        } else {
            logger.info("Cannot save booker, the booker doesnt exist!");
        }

    }

    public void unbooking (Booker booker) {
        if ( eventRepository.isParticipant(booker.getUserId(), booker.getEventId()) ) {
            bookerRepository.delete(booker);
        } else {
            logger.info("Cannot save booker, the booker doesnt exist!");
        }
    }

    public Boolean isBooked (Booker booker) {
        return bookerRepository.isBooked(booker);
    }

    public List<Item> getPopularItems ( Long limit, Long offset ) {
        return itemRepository.getPopularItems(limit, offset);
    }

    public List<Item> getBookedItems ( Long userId ) {
        return itemRepository.getBookedItems( userId );
    }

    public List<Item> getEventBookingItems ( Long eventId ) {
        return itemRepository.getEventBookingItems( eventId );
    }





}
