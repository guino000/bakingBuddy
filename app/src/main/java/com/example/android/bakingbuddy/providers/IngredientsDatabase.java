package com.example.android.bakingbuddy.providers;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.Table;

@Database(version = IngredientsDatabase.VERSION)
public final class IngredientsDatabase {
    public static final int VERSION = 1;
    @Table(IngredientListColumns.class) public static final String INGREDIENTS = "ingredients";
}
