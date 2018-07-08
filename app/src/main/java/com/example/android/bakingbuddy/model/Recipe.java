package com.example.android.bakingbuddy.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingbuddy.R;

import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;

public class Recipe extends AbstractFlexibleItem<Recipe.RecipeViewHolder>{
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

    @Override
    public boolean equals(Object o) {
        if(o instanceof  Recipe){
            Recipe inRecipe = (Recipe) o;
            return String.valueOf(this.mId).equals(String.valueOf(inRecipe.getId()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return String.valueOf(mId).hashCode();
    }

    @Override
    public int getLayoutRes() {
//        TODO: Make Recipe Item Layout
        return 0;
    }

    @Override
    public Recipe.RecipeViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new RecipeViewHolder(view, adapter, true);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, Recipe.RecipeViewHolder holder, int position, List<Object> payloads) {
        holder.mRecipeTitleTextView.setText(mName);
//        TODO: Load Image View using Picasso
    }

    public class RecipeViewHolder extends FlexibleViewHolder{
        public TextView mRecipeTitleTextView;
        public ImageView mRecipeImageView;

        public RecipeViewHolder(View view, FlexibleAdapter adapter, boolean stickyHeader) {
            super(view, adapter, stickyHeader);
//        TODO: Find holder component views on Recipe Item Layout
        }
    }
}
