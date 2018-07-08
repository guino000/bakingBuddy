package com.example.android.bakingbuddy.model;

import java.net.URL;

public class CookingStep {
    private int mID;
    private String mShortDescription;
    private String mDescription;
    private String mVideoUrl;
    private String mThumbURL;

    public CookingStep(int id, String shortDescription, String description, String videoUrl, String thumbUrl){
        mID = id;
        mShortDescription = shortDescription;
        mDescription = description;
        mVideoUrl = videoUrl;
        mThumbURL = thumbUrl;
    }

    public int getID() {
        return mID;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public String getThumbURL() {
        return mThumbURL;
    }
}
