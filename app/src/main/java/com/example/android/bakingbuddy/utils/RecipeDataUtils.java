package com.example.android.bakingbuddy.utils;

import android.util.JsonReader;

import com.example.android.bakingbuddy.model.CookingStep;
import com.example.android.bakingbuddy.model.Ingredient;
import com.example.android.bakingbuddy.model.Recipe;

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
                        getIngredientsFromJSON(jsonObject.optString(KEY_INGREDIENTS)),
                        getCookingStepsFromJSON(jsonObject.optString(KEY_STEPS)),
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
                        jsonObject.optInt(KEY_SERVINGS),
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
}
