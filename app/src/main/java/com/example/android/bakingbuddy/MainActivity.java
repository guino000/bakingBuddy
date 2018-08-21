package com.example.android.bakingbuddy;

import android.content.ContentValues;
import android.content.Intent;
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
import com.example.android.bakingbuddy.providers.RecipesProvider;
import com.example.android.bakingbuddy.utils.RecipeDataUtils;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements
        RecipeAdapter.RecipeAdapterOnClickHandler{
    public static final int ID_RECIPE_JSON_LOADER = 11;
    public static final int ID_RECIPE_CURSOR_LOADER = 12;

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
                if(output == null || output.equals(""))
                    return;
//              Removes old data from database
                getContentResolver().delete(RecipesProvider.Recipes.RECIPES, null, null);

//              Insert new data on database
                ArrayList<Recipe> recipesFromJSON = RecipeDataUtils.getRecipesFromJSON(output);
                ContentValues[] recipeValues = RecipeDataUtils.getRecipeContentValuesFromList(recipesFromJSON);
                getContentResolver().bulkInsert(RecipesProvider.Recipes.RECIPES, recipeValues);

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
        getSupportLoaderManager().initLoader(ID_RECIPE_JSON_LOADER, null, mRecipeJSONLoaderCallbacks);
        getSupportLoaderManager().initLoader(ID_RECIPE_CURSOR_LOADER, null, mRecipeCursorLoaderCallbacks);
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
    public void onClick(int position) {
        Intent intent = new Intent(this, RecipeDetails.class);
        intent.putExtra(RecipeDetails.KEY_INTENT_CLICKED_RECIPE, position);
        startActivity(intent);
    }
}
