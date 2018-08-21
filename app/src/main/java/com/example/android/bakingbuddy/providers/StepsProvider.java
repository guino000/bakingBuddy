package com.example.android.bakingbuddy.providers;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = StepsProvider.AUTHORITY, database = StepsDatabase.class)
public final class StepsProvider {
    public static final String AUTHORITY = "com.example.android.bakingbuddy.steps";

    interface Path{
        String STEPS = "steps";
    }

    @TableEndpoint(table = StepsDatabase.STEPS) public static class Steps {
        @ContentUri(
                path = Path.STEPS,
                type = "vnd.android.cursor.dir/step",
                defaultSort = StepListColumns.DESCRIPTION + " ASC")
        public static final Uri ALL_STEPS = Uri.parse("content://" + AUTHORITY + "/" + Path.STEPS);

        @InexactContentUri(
                path = "/recipes/#/" + Path.STEPS,
                type = "vnd.android.cursor.dir/step",
                name = "RECIPE_ID",
                whereColumn = StepListColumns._FK_RECIPE,
                pathSegment = 1,
                defaultSort = StepListColumns._ID + " ASC")
        public static Uri RECIPE_STEPS(long id){
            return Uri.parse("content://" + AUTHORITY + "/recipes/" + id + "/" + Path.STEPS);
        }
    }
}
