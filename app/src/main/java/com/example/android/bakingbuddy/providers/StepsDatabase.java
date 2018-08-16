package com.example.android.bakingbuddy.providers;

import net.simonvt.schematic.annotation.Database;
import net.simonvt.schematic.annotation.Table;

@Database(version = IngredientsDatabase.VERSION)
public final class StepsDatabase {
    public static final int VERSION = 1;
    @Table(StepListColumns.class) public static final String STEPS = "steps";
}
