package com.example.android.bakingbuddy.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient implements Parcelable {
    private int mQuantity;
    private String mMeasure;
    private String mIngredientName;

    public Ingredient(int quantity, String measure, String ingredientName){
        mQuantity = quantity;
        mMeasure = measure;
        mIngredientName = ingredientName;
    }

    protected Ingredient(Parcel in) {
        mQuantity = in.readInt();
        mMeasure = in.readString();
        mIngredientName = in.readString();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public int getQuantity() {
        return mQuantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public String getIngredientName() {
        return mIngredientName;
    }

    public String getIngredientDescription(){
        return String.format("%s %s %s", String.valueOf(mQuantity), mMeasure, mIngredientName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(mQuantity);
        dest.writeString(mMeasure);
        dest.writeString(mIngredientName);
    }
}
