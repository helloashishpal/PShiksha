package com.example.pshiksha.services;

import java.io.Serializable;

public class ServiceItem implements Serializable {
    private final String title;
    private final Integer imageResId;
    private final Class<?> onClickActivity;

    public ServiceItem(String title, Integer imageResId, Class<?> onClickActivity) {
        this.title = title;
        this.imageResId = imageResId;
        this.onClickActivity = onClickActivity;
    }

    public String getTitle() {
        return title;
    }

    public Integer getImageResId() {
        return imageResId;
    }

    public Class<?> getOnClickActivity() {
        return onClickActivity;
    }
}
