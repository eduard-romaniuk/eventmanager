package com.example.eventmanager.domain;

import lombok.ToString;

import java.time.LocalDateTime;
import java.util.LinkedList;

/**
 * Created by Shvets on 17.04.2018.
 */

@ToString
public class Event {

    private long id;
    private long creatorId;
    private long folderId;
    private String name;
    private long descriptionId;
    private String plase;
    private LocalDateTime timeLineStart;
    private LocalDateTime timeLineFinish;
    private long linkId;
    private int periodInDays;
    private long wishListId;
    private long imageId;
    private boolean isSent;
    private boolean isPrivate;
    private LinkedList<Long> participants;
    private LinkedList<Long> chats;

    public Event copy() {
        return new Event(id, creatorId, folderId, name, descriptionId, plase, timeLineStart, timeLineFinish, linkId, periodInDays, wishListId, imageId, isSent, isPrivate, (LinkedList<Long>)participants.clone(), (LinkedList<Long>)chats.clone());
    }

    public Event() {
    }

    public Event(long id, long creatorId, long folderId, String name, long descriptionId, String plase, LocalDateTime timeLineStart, LocalDateTime timeLineFinish, long linkId, int periodInDays, long wishListId, long imageId, boolean isSent, boolean isPrivate, LinkedList<Long> participants, LinkedList<Long> chats) {
        this.id = id;
        this.creatorId = creatorId;
        this.folderId = folderId;
        this.name = name;
        this.descriptionId = descriptionId;
        this.plase = plase;
        this.timeLineStart = timeLineStart;
        this.timeLineFinish = timeLineFinish;
        this.linkId = linkId;
        this.periodInDays = periodInDays;
        this.wishListId = wishListId;
        this.imageId = imageId;
        this.isSent = isSent;
        this.isPrivate = isPrivate;
        this.participants = participants;
        this.chats = chats;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(long creatorId) {
        this.creatorId = creatorId;
    }

    public long getFolderId() {
        return folderId;
    }

    public void setFolderId(long folderId) {
        this.folderId = folderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getDescriptionId() {
        return descriptionId;
    }

    public void setDescriptionId(long descriptionId) {
        this.descriptionId = descriptionId;
    }

    public String getPlase() {
        return plase;
    }

    public void setPlase(String plase) {
        this.plase = plase;
    }

    public LocalDateTime getTimeLineStart() {
        return timeLineStart;
    }

    public void setTimeLineStart(LocalDateTime timeLineStart) {
        this.timeLineStart = timeLineStart;
    }

    public LocalDateTime getTimeLineFinish() {
        return timeLineFinish;
    }

    public void setTimeLineFinish(LocalDateTime timeLineFinish) {
        this.timeLineFinish = timeLineFinish;
    }

    public long getLinkId() {
        return linkId;
    }

    public void setLinkId(long linkId) {
        this.linkId = linkId;
    }

    public int getPeriodInDays() {
        return periodInDays;
    }

    public void setPeriodInDays(int periodInDays) {
        this.periodInDays = periodInDays;
    }

    public long getWishListId() {
        return wishListId;
    }

    public void setWishListId(long wishListId) {
        this.wishListId = wishListId;
    }

    public long getImageId() {
        return imageId;
    }

    public void setImageId(long imageId) {
        this.imageId = imageId;
    }

    public boolean isSent() {
        return isSent;
    }

    public void setSent(boolean sent) {
        isSent = sent;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        isPrivate = aPrivate;
    }

    public LinkedList<Long> getParticipants() {
        return participants;
    }

    public void setParticipants(LinkedList<Long> participants) {
        this.participants = participants;
    }

    public LinkedList<Long> getChats() {
        return chats;
    }

    public void setChats(LinkedList<Long> chats) {
        this.chats = chats;
    }
}

