package com.example.android.bakingbuddy.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Recipe implements Parcelable{
    private int mId;
    private String mName;
    private Ingredient[] mIngredients;
    private CookingStep[] mSteps;
    private int mServings;
    private String mImagePath;

    protected Recipe(Parcel in) {
        mId = in.readInt();
        mName = in.readString();
        mIngredients = in.createTypedArray(Ingredient.CREATOR);
        mSteps = in.createTypedArray(CookingStep.CREATOR);
        mServings = in.readInt();
        mImagePath = in.readString();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mId);
        dest.writeString(mName);
        dest.writeTypedArray(mIngredients, flags);
        dest.writeTypedArray(mSteps, flags);
        dest.writeInt(mServings);
        dest.writeString(mImagePath);
    }

    public Recipe(int id, String name, Ingredient[] ingredients,
                  CookingStep[] steps, int servings, String imgPath){
        mId = id;
        mName = name;
        mIngredients = ingredients;
        mSteps = steps;
        mServings = servings;
        mImagePath = imgPath;
    }

    public int getId() {
        return mId;
    }

    public String getName(){
        return mName;
    }

    public CookingStep[] getSteps() {
        return mSteps;
    }

    public Ingredient[] getIngredients() {
        return mIngredients;
    }

    public int getServings() {
        return mServings;
    }

    public String getImagePath() {
        return mImagePath;
    }
}
