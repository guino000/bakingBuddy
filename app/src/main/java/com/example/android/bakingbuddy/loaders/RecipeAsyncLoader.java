package com.example.android.bakingbuddy.loaders;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.example.android.bakingbuddy.interfaces.AsyncTaskDelegate;
import com.example.android.bakingbuddy.model.Recipe;
import com.example.android.bakingbuddy.utils.NetworkUtils;

import java.util.List;

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
                    String response = NetworkUtils.getResponseFromHttpUrl(contentUrl);
                    return response;
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
        mDelegate.processFinish(data, loader);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
