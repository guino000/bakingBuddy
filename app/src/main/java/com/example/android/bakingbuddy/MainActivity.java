package com.example.android.bakingbuddy;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ViewTreeObserver;

import com.example.android.bakingbuddy.adapters.RecipeAdapter;
import com.example.android.bakingbuddy.interfaces.AsyncTaskDelegate;
import com.example.android.bakingbuddy.loaders.RecipeAsyncLoader;
import com.example.android.bakingbuddy.loaders.RecipeCursorLoader;
import com.example.android.bakingbuddy.model.Recipe;
import com.example.android.bakingbuddy.providers.IngredientsProvider;
import com.example.android.bakingbuddy.providers.RecipesProvider;
import com.example.android.bakingbuddy.providers.StepsProvider;
import com.example.android.bakingbuddy.utils.RecipeDataUtils;
import com.example.android.bakingbuddy.widget.RecipeWidget;

import java.util.ArrayList;

//TODO: Clean Project and fix layout problems in widget
//TODO: Check why the first ingredient of widget is not correct

public class MainActivity extends AppCompatActivity implements
        RecipeAdapter.RecipeAdapterOnClickHandler{
    public static final int ID_RECIPE_JSON_LOADER = 11;
    public static final int ID_RECIPE_CURSOR_LOADER = 12;

    public static final String SHARED_PREFS_NAME = "com.example.android.bakingbuddy";
    public static final String KEY_SHARED_PREFS_LAST_VIEWED_RECIPE_ID = "last_viewed_recipe";

    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mAdapter;
    private RecipeAsyncLoader mRecipeJSONLoaderCallbacks;
    private RecipeCursorLoader mRecipeCursorLoaderCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Initialize recycler view
        mRecipeRecyclerView = findViewById(R.id.rv_recipes);
        mAdapter = new RecipeAdapter(this);
        mRecipeRecyclerView.setAdapter(mAdapter);
        if(getResources().getConfiguration().screenWidthDp < 600)
            mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                    LinearLayoutManager.VERTICAL, false));
        else {
            mRecipeRecyclerView.setLayoutManager(new GridLayoutManager(this,
                    1, GridLayoutManager.VERTICAL, false));
            ViewTreeObserver viewTreeObserver = mRecipeRecyclerView.getViewTreeObserver();
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    calculateSize();
                }
            });
        }
        mRecipeRecyclerView.setHasFixedSize(true);

//        Initialize loaders and load data
        mRecipeJSONLoaderCallbacks = new RecipeAsyncLoader(this, new AsyncTaskDelegate<String>() {
            @Override
            public void processFinish(String output, Loader<String> callerLoader) {
//                Query new data
                loadRecipesCursor();
            }
        });

        mRecipeCursorLoaderCallbacks = new RecipeCursorLoader(this, mAdapter, new AsyncTaskDelegate<Cursor>() {
            @Override
            public void processFinish(Cursor output, Loader<Cursor> callerLoader) {
                mAdapter.swapCursor(output);
            }
        });

        loadRecipesJSON(getString(R.string.recipes_url));
    }

    private void calculateSize() {
        int spanCount = (int) Math.floor(mRecipeRecyclerView.getWidth() /
                getResources().getDimension(R.dimen.recipe_miniature_width));
        ((GridLayoutManager) mRecipeRecyclerView.getLayoutManager()).setSpanCount(spanCount);
    }

//    Function to load recipes from WEB
    public void loadRecipesJSON(String url){
        LoaderManager.LoaderCallbacks callbacks = mRecipeJSONLoaderCallbacks;
        Bundle queryBundle = new Bundle();
        queryBundle.putString(RecipeAsyncLoader.KEY_ARG_CONTENT_URL, url);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(ID_RECIPE_JSON_LOADER);
        if(loader==null){
            loaderManager.initLoader(ID_RECIPE_JSON_LOADER,queryBundle, mRecipeJSONLoaderCallbacks);
        }else{
            loaderManager.restartLoader(ID_RECIPE_JSON_LOADER,queryBundle,mRecipeJSONLoaderCallbacks);
        }
    }

    //    Function to load recipes from Database
    public void loadRecipesCursor(){
        LoaderManager.LoaderCallbacks callbacks = mRecipeCursorLoaderCallbacks;
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(ID_RECIPE_CURSOR_LOADER);
        if(loader==null){
            loaderManager.initLoader(ID_RECIPE_CURSOR_LOADER,null, callbacks);
        }else{
            loaderManager.restartLoader(ID_RECIPE_CURSOR_LOADER,null,callbacks);
        }
    }

    @Override
    public void onClick(long clickedRecipeID) {
//        Writes to shared preferences the ID of the last clicked recipe
        SharedPreferences preferences = getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE);
        preferences.edit()
                .putLong(KEY_SHARED_PREFS_LAST_VIEWED_RECIPE_ID, clickedRecipeID)
                .apply();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidget.class));
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.lv_widget_ingredients);

        Intent intent = new Intent(this, RecipeDetails.class);
        intent.putExtra(RecipeDetails.KEY_INTENT_CLICKED_RECIPE, clickedRecipeID);
        startActivity(intent);
    }
}
