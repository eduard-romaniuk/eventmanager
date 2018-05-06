package com.example.eventmanager.domain;

import com.example.eventmanager.domain.transfer.validation.WishListValidation;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;
import java.util.List;

public class WishList {

    @Null(groups = WishListValidation.New.class)
    @NotNull(groups = WishListValidation.Exist.class, message = "WishList: Id cannot be null")
    private Long id;

    @NotNull(groups = { WishListValidation.UpdateName.class, WishListValidation.New.class})
    @Size(min=1, max=45)
    private String name;

    @NotNull(groups = WishListValidation.New.class, message = "WishList: Creator id cannot be null")
    @Null(groups = WishListValidation.Exist.class)
    private User user;

    private List<Item> items;

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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Item> getItems() {
        return items;
    }

    public void setItems(List<Item> items) {
        this.items = items;
    }
}
