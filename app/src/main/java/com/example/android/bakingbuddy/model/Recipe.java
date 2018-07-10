package com.example.android.bakingbuddy.model;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingbuddy.R;
import com.squareup.picasso.Picasso;

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
    private final RecipeClickHandler mClickHandler;

    public interface RecipeClickHandler{
        void onClick(Recipe clickedRecipe);
    }

    public Recipe(int id, String name, ArrayList<Ingredient> ingredients,
                  ArrayList<CookingStep> steps, int servings, String imgPath, RecipeClickHandler clickHandler){
        mId = id;
        mName = name;
        mIngredients = ingredients;
        mSteps = steps;
        mServings = servings;
        mImagePath = imgPath;
        mClickHandler = clickHandler;
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
        return R.layout.recipe_miniature;
    }

    @Override
    public Recipe.RecipeViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new RecipeViewHolder(view, adapter, true);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, Recipe.RecipeViewHolder holder, int position, List<Object> payloads) {
        holder.mRecipeTitleTextView.setText(mName);
        if(!"".equals(mImagePath)) {
            Picasso.get()
                    .load(mImagePath)
                    .placeholder(R.drawable.default_placeholder)
                    .error(R.drawable.default_placeholder)
                    .into(holder.mRecipeImageView);
        }else{
            Picasso.get()
                    .load(R.drawable.default_placeholder)
                    .into(holder.mRecipeImageView);
        }
    }

    public class RecipeViewHolder extends FlexibleViewHolder implements View.OnClickListener{
        public TextView mRecipeTitleTextView;
        public ImageView mRecipeImageView;

        public RecipeViewHolder(View view, FlexibleAdapter adapter, boolean stickyHeader) {
            super(view, adapter, stickyHeader);
            mRecipeTitleTextView = view.findViewById(R.id.tv_recipe_name);
            mRecipeImageView = view.findViewById(R.id.img_recipe);
        }

        @Override
        public void onClick(View view) {
//            TODO:Implement Onclick method passing the current Recipe
        }
    }
}
