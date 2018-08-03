package com.example.android.bakingbuddy;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.android.bakingbuddy.model.Recipe;
import com.example.android.bakingbuddy.adapters.RecipeFlexibleItem;
import com.example.android.bakingbuddy.utils.NetworkUtils;
import com.example.android.bakingbuddy.utils.RecipeDataUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<String>{
    public static final int ID_RECIPE_LOADER = 11;
    public static final String OPERATION_URL_EXTRA = "url_that_returns_json";

    private RecyclerView mRecipeRecyclerView;
    private FlexibleAdapter<IFlexible> mAdapter;
    private List<IFlexible> mRecipes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        TODO: If it's a wide screen, the layout manager should be grid
//        Initialize recycler view
        mRecipeRecyclerView = findViewById(R.id.rv_recipes);
        mAdapter = new FlexibleAdapter<>(mRecipes);
        mRecipeRecyclerView.setAdapter(mAdapter);
        mRecipeRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecipeRecyclerView.setHasFixedSize(true);
        FlexibleAdapter.OnItemClickListener clickListener = new FlexibleAdapter.OnItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position) {
                Intent intent = new Intent(view.getContext(), RecipeDetails.class);
                intent.putExtra(RecipeDetails.KEY_INTENT_CLICKED_RECIPE, ((RecipeFlexibleItem) mRecipes.get(position)).getRecipe());
                startActivity(intent);
                return true;
            }
        };
        mAdapter.addListener(clickListener);
//        Initialize recipes dataset
        mRecipes = new ArrayList<>();

//        Initialize JSON Loader and load data
        getSupportLoaderManager().initLoader(ID_RECIPE_LOADER, null, this);
        loadRecipes(getString(R.string.recipes_url));
    }

//    Function to load recipes from WEB
    public void loadRecipes(String url){
        Bundle queryBundle = new Bundle();
        queryBundle.putString(OPERATION_URL_EXTRA, url);
        LoaderManager loaderManager = getSupportLoaderManager();
        Loader<String> loader = loaderManager.getLoader(ID_RECIPE_LOADER);
        if(loader==null){
            loaderManager.initLoader(ID_RECIPE_LOADER,queryBundle,this);
        }else{
            loaderManager.restartLoader(ID_RECIPE_LOADER,queryBundle,this);
        }
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
                    return "";
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
        if("".equals(data)) return;
        mRecipes.clear();
        ArrayList<Recipe> parsedRecipes = RecipeDataUtils.getRecipesFromJSON(data);
        for(Recipe recipe : parsedRecipes){
            mRecipes.add(new RecipeFlexibleItem(recipe));
        }
        mAdapter.updateDataSet(mRecipes,true);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<String> loader) {

    }
}
