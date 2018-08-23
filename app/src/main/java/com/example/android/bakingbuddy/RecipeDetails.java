package com.example.android.bakingbuddy;

import android.app.FragmentManager;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
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
import com.example.android.bakingbuddy.providers.RecipeListColumns;
import com.example.android.bakingbuddy.providers.RecipesProvider;
import com.example.android.bakingbuddy.utils.RecipeDataUtils;
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
    private StepDetailFragment mStepDetailFragment;

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
                if(findViewById(R.id.step_detail_container) == null) {
                    Intent intent = new Intent(view.getContext(), StepDetails.class);
                    ArrayList<CookingStep> steps = new ArrayList<>();
                    for (IFlexible stepFlexibleItem : mSteps) {
                        steps.add(((StepFlexibleItem) stepFlexibleItem).getStep());
                    }
                    intent.putExtra(StepDetails.KEY_INTENT_STEPS_COLLECTION, steps);
                    intent.putExtra(StepDetails.KEY_INTENT_CLICKED_STEP, position);
                    startActivity(intent);
                }else{
                    mStepDetailFragment.updateUiCurrentStep(position);
                }
                return true;
            }
        };
        mStepsAdapter.addListener(clickListener);

//        Get incoming intent
        Intent inIntent = getIntent();
        if(inIntent != null) {
//            Get Recipe from ID
            long clickedRecipeID = inIntent.getLongExtra(KEY_INTENT_CLICKED_RECIPE, 0);
            mInRecipe = RecipeDataUtils.getRecipeObjectFromID(clickedRecipeID, this);

            if (mInRecipe != null) {
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
        }

//        Configure Fragment if is a large view
//        Check if layout is two pane mode
        if(findViewById(R.id.step_detail_container) != null){
            if(savedInstanceState == null) {
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                //            Create new Step Detail Fragment
                ArrayList<CookingStep> steps = new ArrayList<>();
                for (IFlexible stepFlexibleItem : mSteps) {
                    steps.add(((StepFlexibleItem) stepFlexibleItem).getStep());
                }
                mStepDetailFragment = StepDetailFragment.newInstance(
                        steps, 0);
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, mStepDetailFragment)
                        .commit();
            }else{
                mStepDetailFragment = (StepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.step_detail_container);
            }
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
