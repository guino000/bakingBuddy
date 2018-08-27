package com.example.android.bakingbuddy.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingbuddy.R;
import com.example.android.bakingbuddy.model.Ingredient;
import com.example.android.bakingbuddy.providers.IngredientListColumns;
import com.squareup.picasso.Picasso;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientAdapterViewHolder>{
    private Cursor mIngredientCursor;

    public IngredientAdapter(){
    }

    public void swapCursor(Cursor newCursor){
        mIngredientCursor = newCursor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public IngredientAdapter.IngredientAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForGridItem = R.layout.ingredient_item;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForGridItem, parent, false);

        return new IngredientAdapter.IngredientAdapterViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientAdapter.IngredientAdapterViewHolder holder, int position) {
        mIngredientCursor.moveToPosition(position);
        Ingredient ingredient = new Ingredient(
                mIngredientCursor.getInt(mIngredientCursor.getColumnIndex(IngredientListColumns.QUANTITY)),
                mIngredientCursor.getString(mIngredientCursor.getColumnIndex(IngredientListColumns.MEASURE)),
                mIngredientCursor.getString(mIngredientCursor.getColumnIndex(IngredientListColumns.INGREDIENT_NAME))
        );
        holder.mIngredientNameTextView.setText(ingredient.getIngredientDescription());
    }

    @Override
    public int getItemCount() {
        if (mIngredientCursor == null)
            return 0;
        return mIngredientCursor.getCount();
    }

    public class IngredientAdapterViewHolder extends RecyclerView.ViewHolder{
        public TextView mIngredientNameTextView;

        public IngredientAdapterViewHolder(Context context, View itemView) {
            super(itemView);
            mIngredientNameTextView = itemView.findViewById(R.id.tv_ingredient_description);
        }
    }
}
