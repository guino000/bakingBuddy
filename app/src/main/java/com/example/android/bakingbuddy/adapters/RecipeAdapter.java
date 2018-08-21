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
import com.example.android.bakingbuddy.model.Recipe;
import com.example.android.bakingbuddy.providers.RecipeListColumns;
import com.example.android.bakingbuddy.providers.RecipesDatabase;
import com.squareup.picasso.Picasso;

import eu.davidea.flexibleadapter.FlexibleAdapter;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder>{
    private Cursor mRecipeCursor;
    private final RecipeAdapterOnClickHandler mClickHandler;

    public interface RecipeAdapterOnClickHandler{
        void onClick(int position);
    }

    public RecipeAdapter(RecipeAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    public void swapCursor(Cursor newCursor){
        mRecipeCursor = newCursor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForGridItem = R.layout.recipe_miniature;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForGridItem, parent, false);

        return new RecipeAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapterViewHolder holder, int position) {
        mRecipeCursor.moveToPosition(position);
        holder.mRecipeTitleTextView.setText(mRecipeCursor.getString(mRecipeCursor.getColumnIndex(RecipeListColumns.TITLE)));
        String imgPath = mRecipeCursor.getString(mRecipeCursor.getColumnIndex(RecipeListColumns.IMAGE_URL));
        if(!"".equals(imgPath)) {
            Picasso.get()
                    .load(imgPath)
                    .placeholder(R.drawable.default_placeholder)
                    .error(R.drawable.default_placeholder)
                    .into(holder.mRecipeImageView);
        }else{
            Picasso.get()
                    .load(R.drawable.default_placeholder)
                    .into(holder.mRecipeImageView);
        }
    }

    @Override
    public int getItemCount() {
        if (mRecipeCursor == null)
            return 0;
        return mRecipeCursor.getCount();
    }

    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mRecipeTitleTextView;
        public ImageView mRecipeImageView;

        public RecipeAdapterViewHolder(View itemView) {
            super(itemView);
            mRecipeTitleTextView = itemView.findViewById(R.id.tv_recipe_name);
            mRecipeImageView = itemView.findViewById(R.id.img_recipe);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAdapterPosition());
        }
    }
}
