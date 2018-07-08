package com.example.android.bakingbuddy.model;

import java.util.ArrayList;

public class Recipe {
    private int mId;
    private String mName;
    private ArrayList<Ingredient> mIngredients;
    private ArrayList<CookingStep> mSteps;
    private int mServings;
    private String mImagePath;

    public Recipe(int id, String name, ArrayList<Ingredient> ingredients,
                  ArrayList<CookingStep> steps, int servings, String imgPath){
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

    public ArrayList<CookingStep> getSteps() {
        return mSteps;
    }

    public ArrayList<Ingredient> getIngredients() {
        return mIngredients;
    }

    public int getServings() {
        return mServings;
    }

    public String getImagePath() {
        return mImagePath;
    }
}
