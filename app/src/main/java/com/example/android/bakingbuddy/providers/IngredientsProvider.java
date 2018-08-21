package com.example.android.bakingbuddy.providers;

import android.net.Uri;

import net.simonvt.schematic.annotation.ContentProvider;
import net.simonvt.schematic.annotation.ContentUri;
import net.simonvt.schematic.annotation.InexactContentUri;
import net.simonvt.schematic.annotation.TableEndpoint;

@ContentProvider(authority = IngredientsProvider.AUTHORITY, database = IngredientsDatabase.class)
public final class IngredientsProvider {
    public static final String AUTHORITY = "com.example.android.bakingbuddy";

    interface Path{
        String INGREDIENTS = "ingredients";
    }

    @TableEndpoint(table = IngredientsDatabase.INGREDIENTS) public static class Ingredients {
        @ContentUri(
                path = Path.INGREDIENTS,
                type = "vnd.android.cursor.dir/ingredient",
                defaultSort = IngredientListColumns.INGREDIENT_NAME + " ASC")
        public static final Uri INGREDIENTS = Uri.parse("content://" + AUTHORITY + "/recipes/#/ingredients");
    }
}
