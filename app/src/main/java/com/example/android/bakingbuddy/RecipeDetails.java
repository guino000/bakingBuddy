package com.example.android.bakingbuddy;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.example.android.bakingbuddy.adapters.IngredientAdapter;
import com.example.android.bakingbuddy.adapters.StepAdapter;
import com.example.android.bakingbuddy.interfaces.AsyncTaskDelegate;
import com.example.android.bakingbuddy.loaders.IngredientsCursorLoader;
import com.example.android.bakingbuddy.loaders.StepsCursorLoader;
import com.example.android.bakingbuddy.model.CookingStep;
import com.example.android.bakingbuddy.model.Recipe;
import com.example.android.bakingbuddy.utils.RecipeDataUtils;

import java.util.ArrayList;
import java.util.Arrays;

public class RecipeDetails extends AppCompatActivity {

    private static final String TAG = RecipeDetails.class.getSimpleName();

    public  static final int ID_INGREDIENTS_CURSOR_LOADER = 21;
    public  static final int ID_STEPS_CURSOR_LOADER = 22;

    public static final String KEY_INTENT_CLICKED_RECIPE = "clicked_recipe";

    private Recipe mInRecipe;
    private RecyclerView mIngredientsRecyclerView;
    private IngredientAdapter mIngredientsAdapter;
    private RecyclerView mStepsRecyclerView;
    private StepAdapter mStepsAdapter;
    private StepDetailFragment mStepDetailFragment;
    private StepsCursorLoader mStepsLoaderCallbacks;
    private IngredientsCursorLoader mIngredientsLoaderCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        //        Get incoming intent
//        TODO: Replace DB query for something else that does not run on the main thread
        final Intent inIntent = getIntent();
        long clickedRecipeID = inIntent.getLongExtra(KEY_INTENT_CLICKED_RECIPE, 0);
        final Recipe inRecipe = RecipeDataUtils.getRecipeObjectFromID(clickedRecipeID, this);

//            Initialize Ingredients adapters
        mIngredientsRecyclerView = findViewById(R.id.rv_ingredients);
        mIngredientsAdapter = new IngredientAdapter();
        mIngredientsRecyclerView.setAdapter(mIngredientsAdapter);
        mIngredientsRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        mIngredientsRecyclerView.setHasFixedSize(true);
        mIngredientsRecyclerView.setNestedScrollingEnabled(false);

//        Initialize Steps Adapter
        mStepsRecyclerView = findViewById(R.id.rv_steps);
        mStepsAdapter = new StepAdapter(new StepAdapter.StepAdapterOnClickHandler() {
            @Override
            public void onClick(int position) {
                if(findViewById(R.id.step_detail_container) == null) {
                    Intent intent = new Intent(getApplicationContext(), StepDetails.class);
                    ArrayList<CookingStep> steps = null;
                    if(inRecipe.getSteps() != null){
                        steps = new ArrayList<>(Arrays.asList(inRecipe.getSteps()));
                    }
                    intent.putExtra(StepDetails.KEY_INTENT_STEPS_COLLECTION, steps);
                    intent.putExtra(StepDetails.KEY_INTENT_CLICKED_STEP, position);
                    startActivity(intent);
                }else{
                    mStepDetailFragment.updateUiCurrentStep(position);
                }
            }
        });
        mStepsRecyclerView.setAdapter(mStepsAdapter);
        mStepsRecyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        mStepsRecyclerView.setHasFixedSize(true);
        mStepsRecyclerView.setNestedScrollingEnabled(false);

//        Set Up Loaders
        mIngredientsLoaderCallbacks = new IngredientsCursorLoader(this, mIngredientsAdapter, new AsyncTaskDelegate<Cursor>() {
            @Override
            public void processFinish(Cursor output, Loader<Cursor> callerLoader) {
                mIngredientsAdapter.swapCursor(output);
            }
        });

        mStepsLoaderCallbacks = new StepsCursorLoader(this, mStepsAdapter, new AsyncTaskDelegate<Cursor>() {
            @Override
            public void processFinish(Cursor output, Loader<Cursor> callerLoader) {
                mStepsAdapter.swapCursor(output);
            }
        });

//        Start Loaders
        if (inRecipe != null) {
            loadIngredientsCursor(inRecipe.getId());
            loadStepsCursor(inRecipe.getId());
        }

//        Configure Fragment if is a large view
//        Check if layout is two pane mode
        if(findViewById(R.id.step_detail_container) != null){
            mStepDetailFragment = (StepDetailFragment) getSupportFragmentManager().findFragmentById(R.id.step_detail_container);
            if(mStepDetailFragment == null) {
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                //            Create new Step Detail Fragment
                ArrayList<CookingStep> steps = null;
                if (inRecipe != null) {
                    steps = new ArrayList<>(Arrays.asList(inRecipe.getSteps()));
                }
                mStepDetailFragment = StepDetailFragment.newInstance(
                        steps, 0);
                fragmentManager.beginTransaction()
                        .add(R.id.step_detail_container, mStepDetailFragment)
                        .commit();
            }
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void loadIngredientsCursor(long recipeID){
        Bundle args = new Bundle();
        args.putString(IngredientsCursorLoader.ARG_RECIPE_ID, String.valueOf(recipeID));
        android.support.v4.app.LoaderManager.LoaderCallbacks callbacks = mIngredientsLoaderCallbacks;
        android.support.v4.app.LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Cursor> loader = loaderManager.getLoader(ID_INGREDIENTS_CURSOR_LOADER);
        if(loader==null){
            loaderManager.initLoader(ID_INGREDIENTS_CURSOR_LOADER, args, callbacks);
        }else{
            loaderManager.restartLoader(ID_INGREDIENTS_CURSOR_LOADER, args, callbacks);
        }
    }

    private void loadStepsCursor(long recipeID){
        Bundle args = new Bundle();
        args.putString(StepsCursorLoader.ARG_RECIPE_ID, String.valueOf(recipeID));
        android.support.v4.app.LoaderManager.LoaderCallbacks callbacks = mStepsLoaderCallbacks;
        android.support.v4.app.LoaderManager loaderManager = getSupportLoaderManager();
        Loader<Cursor> loader = loaderManager.getLoader(ID_STEPS_CURSOR_LOADER);
        if(loader==null){
            loaderManager.initLoader(ID_STEPS_CURSOR_LOADER, args, callbacks);
        }else{
            loaderManager.restartLoader(ID_STEPS_CURSOR_LOADER, args, callbacks);
        }
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
