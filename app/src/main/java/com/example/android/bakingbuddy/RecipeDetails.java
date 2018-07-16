package com.example.android.bakingbuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.android.bakingbuddy.adapters.IngredientFlexibleItem;
import com.example.android.bakingbuddy.model.Ingredient;
import com.example.android.bakingbuddy.model.Recipe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.IFlexible;

public class RecipeDetails extends AppCompatActivity {

    private static final String TAG = RecipeDetails.class.getSimpleName();
    public static final String KEY_INTENT_CLICKED_RECIPE = "clicked_recipe";

    private Recipe mInRecipe;
    private RecyclerView mIngredientsRecyclerView;
    private FlexibleAdapter<IFlexible> mAdapter;
    private List<IFlexible> mIngredients;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        mIngredientsRecyclerView = findViewById(R.id.rv_ingredients);
        mAdapter = new FlexibleAdapter<>(mIngredients);
        mIngredientsRecyclerView.setAdapter(mAdapter);
        mIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        mIngredientsRecyclerView.setHasFixedSize(true);
        mIngredients = new ArrayList<>();

        Intent inIntent = getIntent();
        if(inIntent != null){
            mInRecipe = inIntent.getParcelableExtra(KEY_INTENT_CLICKED_RECIPE);
            for(Ingredient ingredient : mInRecipe.getIngredients()) {
                mIngredients.add(new IngredientFlexibleItem(ingredient));
            }
            mAdapter.updateDataSet(mIngredients);
//           TODO: Check why the ingredient quantity is always 0
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }
}
