package com.example.android.bakingbuddy.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.JsonReader;

import com.example.android.bakingbuddy.model.CookingStep;
import com.example.android.bakingbuddy.model.Ingredient;
import com.example.android.bakingbuddy.model.Recipe;
import com.example.android.bakingbuddy.providers.IngredientListColumns;
import com.example.android.bakingbuddy.providers.IngredientsProvider;
import com.example.android.bakingbuddy.providers.RecipeListColumns;
import com.example.android.bakingbuddy.providers.RecipesDatabase;
import com.example.android.bakingbuddy.providers.RecipesProvider;
import com.example.android.bakingbuddy.providers.StepListColumns;
import com.example.android.bakingbuddy.providers.StepsProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public final class RecipeDataUtils {
//    Constants for Recipe JSON
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_STEPS = "steps";
    private static final String KEY_INGREDIENTS = "ingredients";
    private static final String KEY_SERVINGS = "servings";
    private static final String KEY_IMAGE = "image";
//    Constants for Ingredient JSON
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_MEASURE = "measure";
    private static final String KEY_INGREDIENT = "ingredient";
//    Constants for Step JSON
    private static final String KEY_STEP_ID = "id";
    private static final String KEY_SHORT_DESCRIPTION = "shortDescription";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_VIDEO_URL = "videoURL";
    private static final String KEY_THUMB_URL = "thumbnailURL";

    public static ArrayList<Recipe> getRecipesFromJSON(String recipesJSON){
        ArrayList<Recipe> recipes = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(recipesJSON);
            for(int i = 0;i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Recipe curRecipe = new Recipe(jsonObject.getInt(KEY_ID),
                        jsonObject.getString(KEY_NAME),
                        getIngredientsFromJSON(jsonObject.optString(KEY_INGREDIENTS)).toArray(new Ingredient[0]),
                        getCookingStepsFromJSON(jsonObject.optString(KEY_STEPS)).toArray(new CookingStep[0]),
                        jsonObject.optInt(KEY_SERVINGS),
                        jsonObject.optString(KEY_IMAGE));
                recipes.add(curRecipe);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return recipes;
    }

    private static  ArrayList<Ingredient> getIngredientsFromJSON(String ingredientsJSON){
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(ingredientsJSON);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                Ingredient curIngredient = new Ingredient(
                        jsonObject.optInt(KEY_QUANTITY),
                        jsonObject.optString(KEY_MEASURE),
                        jsonObject.optString(KEY_INGREDIENT)
                );
                ingredients.add(curIngredient);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return ingredients;
    }

    private static  ArrayList<CookingStep> getCookingStepsFromJSON(String stepsJSON){
        ArrayList<CookingStep> cookingSteps = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(stepsJSON);
            for(int i = 0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                CookingStep curStep = new CookingStep(
                        jsonObject.getInt(KEY_STEP_ID),
                        jsonObject.optString(KEY_SHORT_DESCRIPTION),
                        jsonObject.optString(KEY_DESCRIPTION),
                        jsonObject.optString(KEY_VIDEO_URL),
                        jsonObject.optString(KEY_THUMB_URL)
                );
                cookingSteps.add(curStep);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
        return cookingSteps;
    }

    public static ContentValues[] getRecipeContentValuesFromList(ArrayList<Recipe> recipeArrayList){
        ContentValues[] contentValues = new ContentValues[recipeArrayList.size()];
        for(int i = 0; i< recipeArrayList.size(); i++) {
            ContentValues currentValues = new ContentValues();
            currentValues.put(RecipeListColumns._ID, recipeArrayList.get(i).getId());
            currentValues.put(RecipeListColumns.TITLE, recipeArrayList.get(i).getName());
            currentValues.put(RecipeListColumns.SERVINGS, recipeArrayList.get(i).getServings());
            currentValues.put(RecipeListColumns.IMAGE_URL, recipeArrayList.get(i).getImagePath());
            contentValues[i] = currentValues;
        }
        return contentValues;
    }

    public static ContentValues[] getIngredientContentValuesFromRecipe(Recipe recipe){
        ArrayList<ContentValues> contentValues = new ArrayList<>();
        Ingredient[] ingredients = recipe.getIngredients();
        for (Ingredient ingredient : ingredients) {
            ContentValues currentValues = new ContentValues();
            currentValues.put(IngredientListColumns._FK_RECIPE, recipe.getId());
            currentValues.put(IngredientListColumns.INGREDIENT_NAME, ingredient.getIngredientName());
            currentValues.put(IngredientListColumns.MEASURE, ingredient.getMeasure());
            currentValues.put(IngredientListColumns.QUANTITY, ingredient.getQuantity());
            contentValues.add(currentValues);
        }
        return contentValues.toArray(new ContentValues[contentValues.size()]);
    }

    public static ContentValues[] getCookingStepContentValuesFromRecipe(Recipe recipe){
        ArrayList<ContentValues> contentValues = new ArrayList<>();
        CookingStep[] cookingSteps = recipe.getSteps();
        for (CookingStep cookingStep : cookingSteps) {
            ContentValues currentValues = new ContentValues();
            currentValues.put(StepListColumns._FK_RECIPE, recipe.getId());
            currentValues.put(StepListColumns.SHORT_DESCRIPTION, cookingStep.getShortDescription());
            currentValues.put(StepListColumns.DESCRIPTION, cookingStep.getDescription());
            currentValues.put(StepListColumns.THUMB_URL, cookingStep.getThumbURL());
            currentValues.put(StepListColumns.VIDEO_URL, cookingStep.getVideoUrl());
            contentValues.add(currentValues);
        }
        return contentValues.toArray(new ContentValues[contentValues.size()]);
    }

    public static Recipe getRecipeObjectFromID(long id, Context context){
        try(Cursor cursor = context.getContentResolver().query(RecipesProvider.Recipes.withId(id),
                null,
                null,
                null,
                null)) {
            if (cursor != null) {
                cursor.moveToFirst();
                return new Recipe(
                  cursor.getInt(cursor.getColumnIndex(RecipeListColumns._ID)),
                  cursor.getString(cursor.getColumnIndex(RecipeListColumns.TITLE)),
                  getIngredientsFromRecipeID(id, context),
                  getStepsFromRecipeID(id, context),
                  cursor.getInt(cursor.getColumnIndex(RecipeListColumns.SERVINGS)),
                  cursor.getString(cursor.getColumnIndex(RecipeListColumns.IMAGE_URL))
                );
            }else{
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static CookingStep[] getStepsFromRecipeID(long id, Context context){
        ArrayList<CookingStep> cookingSteps = new ArrayList<>();
        try(Cursor cursor = context.getContentResolver().query(StepsProvider.Steps.RECIPE_STEPS(id),
                null,
                null,
                null,
                null)){
            if (cursor != null) {
                cursor.moveToFirst();
                while(cursor.moveToNext()){
                    cookingSteps.add(new CookingStep(
                            cursor.getInt(cursor.getColumnIndex(StepListColumns._ID)),
                            cursor.getString(cursor.getColumnIndex(StepListColumns.SHORT_DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(StepListColumns.DESCRIPTION)),
                            cursor.getString(cursor.getColumnIndex(StepListColumns.VIDEO_URL)),
                            cursor.getString(cursor.getColumnIndex(StepListColumns.THUMB_URL))
                    ));
                }
                return cookingSteps.toArray(new CookingStep[cookingSteps.size()]);
            }else{
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static Ingredient[] getIngredientsFromRecipeID(long id, Context context){
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        try(Cursor cursor = context.getContentResolver().query(IngredientsProvider.Ingredients.RECIPE_INGREDIENTS(id),
                null,
                null,
                null,
                null)){
            if (cursor != null) {
                cursor.moveToFirst();
                while(cursor.moveToNext()){
                    ingredients.add(new Ingredient(
                            cursor.getInt(cursor.getColumnIndex(IngredientListColumns.QUANTITY)),
                            cursor.getString(cursor.getColumnIndex(IngredientListColumns.MEASURE)),
                            cursor.getString(cursor.getColumnIndex(IngredientListColumns.INGREDIENT_NAME))
                    ));
                }
                return ingredients.toArray(new Ingredient[ingredients.size()]);
            }else{
                return null;
            }
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
