package com.example.android.bakingbuddy.providers;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = RecipesProvider.AUTHORITY, database = RecipesDatabase.class)
public final class StepsProvider {
    public static final String AUTHORITY = "com.example.android.bakingbuddy";

    interface Path{
        String STEPS = "steps";
    }

    @TableEndpoint(table = RecipesDatabase.RECIPES) public static class Recipes {
        @ContentUri(
                path = Path.STEPS,
                type = "vnd.android.cursor.dir/step",
                defaultSort = StepListColumns.DESCRIPTION + " ASC")
        public static final Uri RECIPES = Uri.parse("content://" + AUTHORITY + "/recipes/#/steps");
    }
}
