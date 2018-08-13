package com.example.android.bakingbuddy.providers;

import android.support.annotation.NonNull;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.ExecOnCreate;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.PrimaryKey;
import net.simonvt.schematic.annotation.References;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface IngredientListColumns {
    @DataType(INTEGER) @PrimaryKey
    @AutoIncrement
    String _ID = "_id";
    @DataType(INTEGER)
    String QUANTITY = "quantity";
    @DataType(TEXT)
    String MEASURE = "measure";
    @DataType(TEXT) @NonNull
    String INGREDIENT_NAME = "ingredient_name";
    @DataType(INTEGER) @References(
            table = RecipesDatabase.RECIPES,
            column = RecipeListColumns._ID)
    String _FK_RECIPE = "_fk_recipe";
    @ExecOnCreate String pragma_fk =  "PRAGMA foreign_keys = ON;";
}
