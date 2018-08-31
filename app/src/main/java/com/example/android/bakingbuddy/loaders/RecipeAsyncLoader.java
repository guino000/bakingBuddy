package com.example.android.bakingbuddy.loaders;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.example.android.bakingbuddy.interfaces.AsyncTaskDelegate;
import com.example.android.bakingbuddy.model.Recipe;
import com.example.android.bakingbuddy.providers.IngredientsProvider;
import com.example.android.bakingbuddy.providers.RecipesProvider;
import com.example.android.bakingbuddy.providers.StepsProvider;
import com.example.android.bakingbuddy.utils.NetworkUtils;
import com.example.android.bakingbuddy.utils.RecipeDataUtils;

import java.util.ArrayList;

public class RecipeAsyncLoader implements android.support.v4.app.LoaderManager.LoaderCallbacks<String>{
    public static final String KEY_ARG_CONTENT_URL = "content_url";

    private AsyncTaskDelegate<String> mDelegate;
    private Context mContext;

    public RecipeAsyncLoader(Context context, AsyncTaskDelegate<String> delegate){
        mDelegate = delegate;
        mContext = context;
    }

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<String>(mContext) {
            String parsedJson = "";

            @Override
            protected void onStartLoading() {
                if(args == null) return;

                if(!"".equals(parsedJson))
                    deliverResult(parsedJson);
                else
                    forceLoad();
            }

            @Nullable
            @Override
            public String loadInBackground() {
                if(args == null) return "";
                String contentUrl = args.getString(KEY_ARG_CONTENT_URL);
                try {
                    return NetworkUtils.getResponseFromHttpUrl(contentUrl);
                }catch (Exception e){
                    e.printStackTrace();
                    return "";
                }
            }

            @Override
            public void deliverResult(@Nullable String data) {
                parsedJson = data;
                super.deliverResult(data);
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
        if(data == null || data.equals(""))
            return;
//              Removes old data from database
        mContext.getContentResolver().delete(RecipesProvider.Recipes.RECIPES, null, null);
        mContext.getContentResolver().delete(StepsProvider.Steps.ALL_STEPS, null, null);
        mContext.getContentResolver().delete(IngredientsProvider.Ingredients.ALL_INGREDIENTS, null, null);
//              Insert new data on database
        ArrayList<Recipe> recipesFromJSON = RecipeDataUtils.getRecipesFromJSON(data);
        ContentValues[] recipeValues = RecipeDataUtils.getRecipeContentValuesFromList(recipesFromJSON);

        mContext.getContentResolver().bulkInsert(RecipesProvider.Recipes.RECIPES, recipeValues);
        for(Recipe recipe : recipesFromJSON) {
            ContentValues[] ingredientValues = RecipeDataUtils.getIngredientContentValuesFromRecipe(recipe);
            ContentValues[] stepValues = RecipeDataUtils.getCookingStepContentValuesFromRecipe(recipe);
            mContext.getContentResolver().bulkInsert(IngredientsProvider.Ingredients.RECIPE_INGREDIENTS(recipe.getId()), ingredientValues);
            mContext.getContentResolver().bulkInsert(StepsProvider.Steps.RECIPE_STEPS(recipe.getId()), stepValues);
        }
        mDelegate.processFinish(data, loader);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
