package com.example.android.bakingbuddy.model;

public class Ingredient {
    private int mQuantity;
    private String mMeasure;
    private String mIngredientName;

    public Ingredient(int quantity, String measure, String ingredientName){
        mQuantity = quantity;
        mMeasure = measure;
        mIngredientName = ingredientName;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public String getIngredientName() {
        return mIngredientName;
    }
}
