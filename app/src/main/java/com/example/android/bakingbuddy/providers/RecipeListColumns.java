package com.example.android.bakingbuddy.providers;

import android.support.annotation.NonNull;

import net.simonvt.schematic.annotation.AutoIncrement;
import net.simonvt.schematic.annotation.DataType;
import net.simonvt.schematic.annotation.OnCreate;
import net.simonvt.schematic.annotation.PrimaryKey;

import static net.simonvt.schematic.annotation.DataType.Type.INTEGER;
import static net.simonvt.schematic.annotation.DataType.Type.TEXT;

public interface RecipeListColumns {
    @DataType(INTEGER) @PrimaryKey @AutoIncrement String _ID = "_id";
    @DataType(TEXT) @NonNull String TITLE = "title";
    @DataType(INTEGER) @NonNull String SERVINGS = "servings";
    @DataType(TEXT) String IMAGE_URL = "image_url";
}
