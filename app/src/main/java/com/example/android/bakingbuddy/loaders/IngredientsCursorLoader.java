package com.example.android.bakingbuddy.loaders;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.example.android.bakingbuddy.adapters.IngredientAdapter;
import com.example.android.bakingbuddy.interfaces.AsyncTaskDelegate;
import com.example.android.bakingbuddy.providers.IngredientsProvider;

public class IngredientsCursorLoader implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>  {
//    Args keys
    public static final String ARG_RECIPE_ID = "recipe_id";

    private Context mContext;
    private IngredientAdapter mAdapter;
    private AsyncTaskDelegate<Cursor> mDelegate;

    public IngredientsCursorLoader(Context context, IngredientAdapter adapter, AsyncTaskDelegate<Cursor> delegate){
        mContext = context;
        mDelegate = delegate;
        mAdapter = adapter;
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        Uri contentUri;
        if(args!=null){
            contentUri = IngredientsProvider.Ingredients.RECIPE_INGREDIENTS(Long.valueOf(args.getString(ARG_RECIPE_ID)));
        }else{
            contentUri = IngredientsProvider.Ingredients.ALL_INGREDIENTS;
        }

        return new CursorLoader(mContext,
                contentUri,
                null,
                null,
                null,
                null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        mDelegate.processFinish(data, loader);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
