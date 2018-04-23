package com.example.eventmanager.domain;

import lombok.ToString;
import org.springframework.stereotype.Component;

@Component
@ToString
public class Image {
    private Long id;
    private String url;

    public Image() {
    }

    public Image(String url) {
        this.url = url;
    }

    public Image(Long id, String url) {
        this.id = id;
        this.url = url;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}