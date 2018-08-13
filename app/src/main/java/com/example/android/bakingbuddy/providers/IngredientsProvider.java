package com.example.android.bakingbuddy.providers;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = IngredientsProvider.AUTHORITY, database = IngredientsDatabase.class)
public final class IngredientsProvider {
    public static final String AUTHORITY = "com.example.android.bakingbuddy.IngredientsProvider";

    interface Path{
        String INGREDIENTS = "ingredients";
    }

    @TableEndpoint(table = IngredientsDatabase.INGREDIENTS) public static class Ingredients {
        @ContentUri(
                path = "ingredients",
                type = "vnd.android.cursor.dir/ingredient",
                defaultSort = IngredientListColumns.INGREDIENT_NAME + " ASC")
        public static final Uri INGREDIENTS = Uri.parse("content://" + AUTHORITY + "/ingredients");

        @InexactContentUri(
                path = Path.INGREDIENTS + "/#",
                name = "INGREDIENT_ID",
                type = "vnd.android.cursor.item/ingredient",
                whereColumn = IngredientListColumns._ID,
                pathSegment = 1)
        public static Uri withId(long id){
            return Uri.parse("content://" + AUTHORITY + "/ingredients/" + id);
        }
    }
}
