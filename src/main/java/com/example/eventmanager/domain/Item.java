package com.example.eventmanager.domain;

import com.example.eventmanager.domain.transfer.validation.ItemValidation;
import com.example.eventmanager.domain.transfer.view.ItemView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;

@Data
public class Item {

    @JsonView({ItemView.ShortView.class})
    @NotNull(groups = ItemValidation.Exist.class, message = "Id cannot be null")
    private Long id;

    @JsonView({ItemView.ShortView.class})
    @NotNull(groups = ItemValidation.New.class, message = "Name cannot be null")
    @Size(min=1, max=45)
    private String name;

    @JsonView({ItemView.ShortView.class})
    @NotNull(groups = ItemValidation.New.class, message = "Priority cannot be null")
    @Min(value = 0, message = "Priority should be between 0..2")
    @Max(value = 2, message = "Priority should be between 0..2")
    private Integer priority;

    @JsonView({ItemView.FullView.class})
    @Size(max=2083)
    private String description;

    @JsonView({ItemView.ShortView.class})
    @NotNull(groups = ItemValidation.New.class, message = "WishList id cannot be null")
    private Long wishListId;

    @JsonView({ItemView.ShortView.class})
    @Null(groups = ItemValidation.New.class)
    @NotNull(groups = ItemValidation.Exist.class)
    private Integer likes;


    @JsonView({ItemView.FullView.class})
    private List<Tag> tags;

    @JsonView({ItemView.FullView.class})
    private List<String> images;

    @JsonView({ItemView.FullView.class})
    private List<Booker> bookers;

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

    public Long getWishListId() {
        return wishListId;
    }

    public void setWishListId(Long wishListId) {
        this.wishListId = wishListId;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }

    public Integer getLikes() {
        return likes;
    }

    public void setLikes(Integer likes) {
        this.likes = likes;
    }


    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public List<Booker> getBookers() {
        return bookers;
    }

    public void setBookers(List<Booker> bookers) {
        this.bookers = bookers;
    }

    @Override
    public String toString() {

        return "Item{" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", priority='" + priority + '\'' +
                ", wishlist id=" + wishListId +
                ", likes id=" + likes +
                tags
                +
                '}';
    }
}
