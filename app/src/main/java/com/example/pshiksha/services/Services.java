package com.example.pshiksha.services;

import android.app.Activity;

public class Services {
    private final String title;
    private final Integer imageResId;
    private final Integer colorResID;
    private final Activity onClickActivity;

    public Services(String title, Integer imageResId, Integer colorResID, Activity onClickActivity) {
        this.title = title;
        this.imageResId = imageResId;
        this.colorResID = colorResID;
        this.onClickActivity = onClickActivity;
    }

    public String getTitle() {
        return title;
    }

    public Integer getImageResId() {
        return imageResId;
    }

    public Integer getColorResID() {
        return colorResID;
    }

    public Activity getOnClickActivity() {
        return onClickActivity;
    }
}
