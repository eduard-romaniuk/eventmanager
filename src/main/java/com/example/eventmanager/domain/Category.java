package com.example.eventmanager.domain;


import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

@Data
public class Category {

    @JsonView(EventView.FullView.class)
    private Long id;
    @JsonView(EventView.FullView.class)
    private String name;

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
}
