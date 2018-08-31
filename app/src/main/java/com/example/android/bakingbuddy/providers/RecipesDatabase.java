package com.example.android.bakingbuddy.providers;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = RecipesDatabase.VERSION)
public final class RecipesDatabase {
    public static final int VERSION = 2;
    @Table(RecipeListColumns.class) public static final String RECIPES = "recipes";
}
