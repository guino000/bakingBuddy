package com.example.android.bakingbuddy.widget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingbuddy.MainActivity;
import com.example.android.bakingbuddy.R;
import com.example.android.bakingbuddy.model.Ingredient;
import com.example.android.bakingbuddy.providers.IngredientListColumns;
import com.example.android.bakingbuddy.providers.IngredientsProvider;

public class IngredientsWidgetService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new IngredientsRemoteViewsFactory(this.getApplicationContext());
    }

    class IngredientsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{
        private Cursor mCursor;
        private Context mContext;
        private long mSelectedRecipeID;

        public IngredientsRemoteViewsFactory(Context context){
            mContext = context;
        }

        @Override
        public void onCreate() {

        }

        @Override
        public void onDataSetChanged() {
            if(mCursor != null) mCursor.close();
            SharedPreferences preferences = mContext.getSharedPreferences(MainActivity.SHARED_PREFS_NAME, Context.MODE_PRIVATE);
            mSelectedRecipeID = preferences.getLong(MainActivity.KEY_SHARED_PREFS_LAST_VIEWED_RECIPE_ID,0);
            mCursor = mContext.getContentResolver().query(
                    IngredientsProvider.Ingredients.RECIPE_INGREDIENTS(mSelectedRecipeID),
                    null,
                    null,
                    null,
                    null
            );
        }

        @Override
        public void onDestroy() {
            mCursor.close();
        }

        @Override
        public int getCount() {
            return mCursor.getCount();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            if(mCursor == null || mCursor.getCount() == 0) return null;
            RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_ingredient_item);
            mCursor.moveToPosition(position);
            Ingredient ingredient = new Ingredient(
                    mCursor.getInt(mCursor.getColumnIndex(IngredientListColumns.QUANTITY)),
                    mCursor.getString(mCursor.getColumnIndex(IngredientListColumns.MEASURE)),
                    mCursor.getString(mCursor.getColumnIndex(IngredientListColumns.INGREDIENT_NAME))
            );
            remoteViews.setTextViewText(R.id.tv_widget_ingredient_description, ingredient.getIngredientDescription());
            return remoteViews;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }
    }
}
