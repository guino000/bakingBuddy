package com.example.android.bakingbuddy.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.android.bakingbuddy.R;
import com.example.android.bakingbuddy.model.CookingStep;

import org.w3c.dom.Text;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;

public class StepFlexibleItem extends AbstractFlexibleItem<StepFlexibleItem.StepViewHolder>{
    private CookingStep mStep;

    public StepFlexibleItem(CookingStep inStep){
        mStep = inStep;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof CookingStep){
            CookingStep inStep = (CookingStep) o;
            return inStep.getID() == mStep.getID() && inStep.getShortDescription().equals(mStep.getShortDescription());
        }
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.step_miniature;
    }

    @Override
    public StepFlexibleItem.StepViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new StepViewHolder(view, adapter);
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, StepFlexibleItem.StepViewHolder holder, int position, List<Object> payloads) {
        holder.mShortDescriptionTextView.setText(mStep.getShortDescription());
        holder.mDescriptionTextView.setText(mStep.getDescription());
    }

    public class StepViewHolder extends FlexibleViewHolder{
        public TextView mShortDescriptionTextView;
        public TextView mDescriptionTextView;

        public StepViewHolder(View view, FlexibleAdapter adapter) {
            super(view, adapter);
            mShortDescriptionTextView = view.findViewById(R.id.tv_step_short_description);
            mDescriptionTextView = view.findViewById(R.id.tv_step_description);
        }
    }
}
