package com.example.android.bakingbuddy.providers;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = RecipesProvider.AUTHORITY, database = RecipesDatabase.class)
public final class RecipesProvider {
    public static final String AUTHORITY = "com.example.android.bakingbuddy.RecipesProvider";

    interface Path{
        String RECIPES = "recipes";
    }

    @TableEndpoint(table = RecipesDatabase.RECIPES) public static class Recipes {
        @ContentUri(
                path = "recipes",
                type = "vnd.android.cursor.dir/recipe",
                defaultSort = RecipeListColumns.TITLE + " ASC")
        public static final Uri RECIPES = Uri.parse("content://" + AUTHORITY + "/recipes");

        @InexactContentUri(
                path = Path.RECIPES + "/#",
                name = "RECIPE_ID",
                type = "vnd.android.cursor.item/recipe",
                whereColumn = RecipeListColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id){
            return Uri.parse("content://" + AUTHORITY + "/recipes/" + id);
        }
    }
}
