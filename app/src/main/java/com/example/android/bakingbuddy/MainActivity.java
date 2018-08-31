package com.example.android.bakingbuddy;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
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
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.bakingbuddy.adapters.RecipeAdapter;
import com.example.android.bakingbuddy.interfaces.AsyncTaskDelegate;
import com.example.android.bakingbuddy.loaders.RecipeAsyncLoader;
import com.example.android.bakingbuddy.loaders.RecipeCursorLoader;
import com.example.android.bakingbuddy.utils.NetworkUtils;
import com.example.android.bakingbuddy.widget.RecipeWidget;

//TODO: Clean Project and fix layout problems in widget

public class MainActivity extends AppCompatActivity implements
        RecipeAdapter.RecipeAdapterOnClickHandler{
    private static final int ID_RECIPE_JSON_LOADER = 11;
    private static final int ID_RECIPE_CURSOR_LOADER = 12;

    public static final String SHARED_PREFS_NAME = "com.example.android.bakingbuddy";
    public static final String KEY_SHARED_PREFS_LAST_VIEWED_RECIPE_ID = "last_viewed_recipe";

    private RecyclerView mRecipeRecyclerView;
    private RecipeAdapter mAdapter;
    private RecipeAsyncLoader mRecipeJSONLoaderCallbacks;
    private RecipeCursorLoader mRecipeCursorLoaderCallbacks;
    private ProgressBar mProgressBar;
    private TextView mErrorTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Initialize recycler view
        mProgressBar = findViewById(R.id.pb_main_activity);
        mErrorTextView = findViewById(R.id.tv_error_msg_main_activity);
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
                setProgressBarVisible(false);
                mAdapter.swapCursor(output);
                if (!(output.moveToFirst()) || output.getCount() == 0){
                    setErrorMessageVisible(true);
                }
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
private void loadRecipesJSON(String url){
        if(!NetworkUtils.isOnline(this)){
            setErrorMessageVisible(true);
            return;
        }
        setProgressBarVisible(true);
        setErrorMessageVisible(false);
        LoaderManager.LoaderCallbacks callbacks = mRecipeJSONLoaderCallbacks;
        Bundle queryBundle = new Bundle();
        queryBundle.putString(RecipeAsyncLoader.KEY_ARG_CONTENT_URL, url);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(ID_RECIPE_JSON_LOADER);
        if(loader==null){
            loaderManager.initLoader(ID_RECIPE_JSON_LOADER,queryBundle, callbacks);
        }else{
            loaderManager.restartLoader(ID_RECIPE_JSON_LOADER,queryBundle,callbacks);
        }
    }

    //    Function to load recipes from Database
    private void loadRecipesCursor(){
        LoaderManager.LoaderCallbacks callbacks = mRecipeCursorLoaderCallbacks;
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Cursor> loader = loaderManager.getLoader(ID_RECIPE_CURSOR_LOADER);
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

//    Control UI widgets
    private void setErrorMessageVisible(boolean visible){
        if (visible){
            mRecipeRecyclerView.setVisibility(View.INVISIBLE);
            mErrorTextView.setVisibility(View.VISIBLE);
        }else{
            mRecipeRecyclerView.setVisibility(View.VISIBLE);
            mErrorTextView.setVisibility(View.INVISIBLE);
        }
    }

    private void setProgressBarVisible(boolean visible){
        if (visible){
            mRecipeRecyclerView.setVisibility(View.INVISIBLE);
            mProgressBar.setVisibility(View.VISIBLE);
        }else{
            mRecipeRecyclerView.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
