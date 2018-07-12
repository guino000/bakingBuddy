package com.example.android.bakingbuddy.adapters;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingbuddy.R;
import com.example.android.bakingbuddy.model.Recipe;
import com.squareup.picasso.Picasso;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;

public class RecipeFlexibleItem extends AbstractFlexibleItem<RecipeFlexibleItem.RecipeViewHolder>{
    private final Recipe mRecipe;

    public interface RecipeClickHandler{
        void onClick(Recipe clickedRecipe);
    }

    public RecipeFlexibleItem(Recipe inRecipe){
        mRecipe = inRecipe;
    }

    public Recipe getRecipe(){
        return mRecipe;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof  Recipe){
            Recipe inRecipe = (Recipe) o;
            return String.valueOf(mRecipe.getId()).equals(String.valueOf(inRecipe.getId()));
        }
        return false;
    }

    @Override
    public int hashCode() {
        return String.valueOf(mRecipe.getId()).hashCode();
    }

    @Override
    public int getLayoutRes() {
        return R.layout.recipe_miniature;
    }

    @Override
    public RecipeFlexibleItem.RecipeViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new RecipeViewHolder(view, adapter, true);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, RecipeFlexibleItem.RecipeViewHolder holder, int position, List<Object> payloads) {
        holder.mRecipeTitleTextView.setText(mRecipe.getName());
        if(!"".equals(mRecipe.getImagePath())) {
            Picasso.get()
                    .load(mRecipe.getImagePath())
                    .placeholder(R.drawable.default_placeholder)
                    .error(R.drawable.default_placeholder)
                    .into(holder.mRecipeImageView);
        }else{
            Picasso.get()
                    .load(R.drawable.default_placeholder)
                    .into(holder.mRecipeImageView);
        }
    }

    public class RecipeViewHolder extends FlexibleViewHolder{
        public TextView mRecipeTitleTextView;
        public ImageView mRecipeImageView;

        public RecipeViewHolder(View view, FlexibleAdapter adapter, boolean stickyHeader) {
            super(view, adapter, stickyHeader);
            mRecipeTitleTextView = view.findViewById(R.id.tv_recipe_name);
            mRecipeImageView = view.findViewById(R.id.img_recipe);
        }
    }
}
