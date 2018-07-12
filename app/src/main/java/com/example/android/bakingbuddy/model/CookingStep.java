package com.example.android.bakingbuddy.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.net.URL;

public class CookingStep implements Parcelable{
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

    protected CookingStep(Parcel in) {
        mID = in.readInt();
        mShortDescription = in.readString();
        mDescription = in.readString();
        mVideoUrl = in.readString();
        mThumbURL = in.readString();
    }

    public static final Creator<CookingStep> CREATOR = new Creator<CookingStep>() {
        @Override
        public CookingStep createFromParcel(Parcel in) {
            return new CookingStep(in);
        }

        @Override
        public CookingStep[] newArray(int size) {
            return new CookingStep[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mID);
        dest.writeString(mShortDescription);
        dest.writeString(mDescription);
        dest.writeString(mVideoUrl);
        dest.writeString(mThumbURL);
    }
}
