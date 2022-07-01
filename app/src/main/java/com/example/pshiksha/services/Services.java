package com.example.pshiksha.services;

public class Services {
    private final String title;
    private final Integer imageResId;
    private final Integer color;

    public Services(String title, Integer imageResId, Integer color) {
        this.title = title;
        this.imageResId = imageResId;
        this.color = color;
    }

    public String getTitle() {
        return title;
    }

    public Integer getImageResId() {
        return imageResId;
    }

    public Integer getColor() {
        return color;
    }
}
