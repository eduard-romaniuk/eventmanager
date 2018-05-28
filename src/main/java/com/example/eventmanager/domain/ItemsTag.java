package com.example.eventmanager.domain;

import lombok.Data;

@Data
public class ItemsTag {
    private Item item;
    private Long tagId;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public Long getTagId() {
        return tagId;
    }

    public void setTagId(Long tagId) {
        this.tagId = tagId;
    }
}
