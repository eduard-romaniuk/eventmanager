package com.example.eventmanager.domain;

import lombok.Data;

import java.util.List;

@Data
public class Item {
    private Long id;
    private String name;
    private Integer priority;
    private String description;
    private WishList wishList;
    private List<Like> likes;
    private List<Tag> tags;


    private Integer likeCounts;
//    private List<Image> images;
//    private Booker booker;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public WishList getWishList() {
        return wishList;
    }

    public void setWishList(WishList wishList) {
        this.wishList = wishList;
    }

    public List<Like> getLikes() {
        return likes;
    }

    public void setLikes(List<Like> likes) {
        this.likes = likes;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Integer getLikeCounts() {
        return likeCounts;
    }

    public void setLikeCounts(Integer likeCounts) {
        this.likeCounts = likeCounts;
    }


    //
//    public List<Image> getImages() {
//        return images;
//    }
//
//    public void setImages(List<Image> images) {
//        this.images = images;
//    }
//
//    public Booker getBooker() {
//        return booker;
//    }
//
//    public void setBooker(Booker booker) {
//        this.booker = booker;
//    }


    @Override
    public String toString() {
        return "Item{" +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priority='" + priority + '\'' +
                ", wishlist=" + wishList.getId() +
                '}';
    }
}
