package com.example.android.bakingbuddy.loaders;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.example.android.bakingbuddy.adapters.RecipeAdapter;
import com.example.android.bakingbuddy.interfaces.AsyncTaskDelegate;
import com.example.android.bakingbuddy.providers.RecipesProvider;

public class RecipeCursorLoader implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor> {

//    Args Keys
    public static final String ARG_RECIPE_ID = "recipe_id";

    private Context mContext;
    private RecipeAdapter mAdapter;
    private AsyncTaskDelegate<Cursor> mDelegate;

    public RecipeCursorLoader(Context context, RecipeAdapter adapter, AsyncTaskDelegate<Cursor> delegate){
        mContext = context;
        mDelegate = delegate;
        mAdapter = adapter;
    }

    @NonNull
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri contentUri;
        if(args!=null){
            contentUri = RecipesProvider.Recipes.withId(
                    Long.valueOf(args.getString(ARG_RECIPE_ID)));
        }else{
            contentUri = RecipesProvider.Recipes.RECIPES;
        }

        return  new android.support.v4.content.CursorLoader(mContext,
                contentUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        mDelegate.processFinish(data, loader);
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader loader) {
        mAdapter.swapCursor(null);
    }
}
