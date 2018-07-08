package com.example.android.bakingbuddy;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.bakingbuddy.model.Recipe;
import com.example.android.bakingbuddy.utils.NetworkUtils;

import java.io.IOException;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String> {
    public static final int ID_RECIPE_LOADER = 11;
    public static final String OPERATION_URL_EXTRA = "url_that_returns_json";

    private FlexibleAdapter<IFlexible> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//      TODO: Create and initialize recipe recycler view
//      TODO: Initialize Flexible Adapter

//        Initialize JSON Loader
        getSupportLoaderManager().initLoader(ID_RECIPE_LOADER, null, this);
    }

//    Recipe Async Task Loader Callbacks
    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Loader<String> onCreateLoader(int id, @Nullable final Bundle args) {
        return new AsyncTaskLoader<String>(this) {
            @Nullable
            @Override
            public String loadInBackground() {
//                Get URL and check if its null
                String url = args.getString(OPERATION_URL_EXTRA);
                if(url != null && "".equals(url)){
                    return null;
                }

                String operationResultString="";
                try{
                    operationResultString = NetworkUtils.getResponseFromHttpUrl(url);
                }catch (IOException e){
                    e.printStackTrace();
                }
                return operationResultString;
            }

            @Override
            protected void onStartLoading() {
                forceLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(@NonNull Loader<String> loader, String data) {
//        TODO: Transform Json into Recipe Objects and Update RecyclerView Dataset
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
