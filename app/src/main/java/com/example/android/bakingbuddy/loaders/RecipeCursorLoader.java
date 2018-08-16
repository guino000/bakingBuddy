package com.example.android.bakingbuddy.loaders;

import android.app.LoaderManager;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.example.android.bakingbuddy.adapters.RecipeAdapter;
import com.example.android.bakingbuddy.interfaces.AsyncTaskDelegate;
import com.example.android.bakingbuddy.providers.RecipesProvider;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

public class RecipeCursorLoader implements LoaderManager.LoaderCallbacks<Cursor> {

//    Args Keys
    public static final String ARG_RECIPE_ID = "recipe_id";

    private Context mContext;
    private RecipeAdapter mAdapter;
    private AsyncTaskDelegate<Cursor> mDelegate;

    public RecipeCursorLoader(Context context, AsyncTaskDelegate<Cursor> delegate, RecipeAdapter adapter){
        mContext = context;
        mDelegate = delegate;
        mAdapter = adapter;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri contentUri;
        if(args!=null){
            contentUri = RecipesProvider.Recipes.withId(
                    Long.valueOf(args.getString(ARG_RECIPE_ID)));
        }else{
            contentUri = RecipesProvider.Recipes.RECIPES;
        }

        return  new CursorLoader(mContext,
                contentUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mDelegate.processFinish(data, loader);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
