package com.example.android.bakingbuddy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import com.example.android.bakingbuddy.adapters.IngredientFlexibleItem;
import com.example.android.bakingbuddy.adapters.StepFlexibleItem;
import com.example.android.bakingbuddy.model.CookingStep;
import com.example.android.bakingbuddy.model.Ingredient;
import com.example.android.bakingbuddy.model.Recipe;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;

import net.alexandroid.utils.exoplayerhelper.ExoPlayerHelper;

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
    private FlexibleAdapter<IFlexible> mIngredientsAdapter;
    private List<IFlexible> mIngredients;
    private RecyclerView mStepsRecyclerView;
    private FlexibleAdapter<IFlexible> mStepsAdapter;
    private List<IFlexible> mSteps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

//            Initialize Ingredients adapters
        mIngredientsRecyclerView = findViewById(R.id.rv_ingredients);
        mIngredientsAdapter = new FlexibleAdapter<>(mIngredients);
        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);
        mIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        mIngredientsRecyclerView.setHasFixedSize(true);
        mIngredientsRecyclerView.setNestedScrollingEnabled(false);
        mIngredients = new ArrayList<>();

//        Initialize Steps Adapter
        mStepsRecyclerView = findViewById(R.id.rv_steps);
        mStepsAdapter = new FlexibleAdapter<>(mSteps);
        mStepsRecyclerView.setAdapter(mStepsAdapter);
        mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mStepsRecyclerView.setHasFixedSize(true);
        mStepsRecyclerView.setNestedScrollingEnabled(false);
        mSteps = new ArrayList<>();

//        Configure more details click listener
        FlexibleAdapter.OnItemClickListener clickListener = new FlexibleAdapter.OnItemClickListener() {
            @Override
            public boolean onItemClick(View view, int position) {
                Intent intent = new Intent(view.getContext(),StepDetails.class);
                ArrayList<CookingStep> steps = new ArrayList<>();
                for(IFlexible stepFlexibleItem : mSteps) {
                    steps.add(((StepFlexibleItem) stepFlexibleItem).getStep());
                }
                intent.putExtra(StepDetails.KEY_INTENT_STEPS_COLLECTION, steps);
                intent.putExtra(StepDetails.KEY_INTENT_CLICKED_STEP, position);
                startActivity(intent);
                return true;
            }
        };
        mStepsAdapter.addListener(clickListener);

        Intent inIntent = getIntent();
        if(inIntent != null) {
            mInRecipe = inIntent.getParcelableExtra(KEY_INTENT_CLICKED_RECIPE);
//            Update Ingredients
            for (Ingredient ingredient : mInRecipe.getIngredients()) {
                mIngredients.add(new IngredientFlexibleItem(ingredient));
            }
            mIngredientsAdapter.updateDataSet(mIngredients);

//            Update Steps
            for (CookingStep step : mInRecipe.getSteps()) {
                mSteps.add(new StepFlexibleItem(step));
            }
            mStepsAdapter.updateDataSet(mSteps);
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedID = item.getItemId();

        switch (selectedID){
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                throw  new UnsupportedOperationException();
        }

        return super.onOptionsItemSelected(item);
    }
}
