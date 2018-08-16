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
import com.example.android.bakingbuddy.model.CookingStep;
import com.example.android.bakingbuddy.providers.StepListColumns;
import com.squareup.picasso.Picasso;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepAdapterViewHolder>{
    private Cursor mStepCursor;
    private final StepAdapter.StepAdapterOnClickHandler mClickHandler;

    public interface StepAdapterOnClickHandler{
        void onClick(int position);
    }

    public StepAdapter(StepAdapter.StepAdapterOnClickHandler clickHandler){
        mClickHandler = clickHandler;
    }

    public void swapCursor(Cursor newCursor){
        mStepCursor = newCursor;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public StepAdapter.StepAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        int layoutIdForGridItem = R.layout.step_miniature;
        LayoutInflater inflater = LayoutInflater.from(context);

        View view = inflater.inflate(layoutIdForGridItem, parent, false);

        return new StepAdapter.StepAdapterViewHolder(parent.getContext(), view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepAdapter.StepAdapterViewHolder holder, int position) {
        mStepCursor.moveToPosition(position);
        holder.mShortDescriptionTextView.setText(
                mStepCursor.getString(mStepCursor.getColumnIndex(StepListColumns.SHORT_DESCRIPTION)));
    }

    @Override
    public int getItemCount() {
        if (mStepCursor == null)
            return 0;
        return mStepCursor.getCount();
    }

    public class StepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView mShortDescriptionTextView;

        public StepAdapterViewHolder(Context context, View itemView) {
            super(itemView);
            mShortDescriptionTextView = itemView.findViewById(R.id.tv_step_short_description);
        }

        @Override
        public void onClick(View v) {
            mClickHandler.onClick(getAdapterPosition());
        }
    }
}
