package com.example.eventmanager.domain;

import com.example.eventmanager.domain.transfer.view.ItemView;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

@Data
public class Tag {

    @JsonView({ItemView.FullView.class})
    private Long id;

    @JsonView({ItemView.FullView.class})
    @NotNull(message = "Tag name cannot be null")
    @Size(min=1, max=30)
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

    @Override
    public String toString() {
        return "{ tag_id: " + id + ", tag_name: " + name + "}";
    }
}
