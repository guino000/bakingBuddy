package com.example.android.bakingbuddy.adapters;

import android.view.View;
import android.widget.TextView;

import com.example.android.bakingbuddy.R;
import com.example.android.bakingbuddy.model.Ingredient;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;

public class IngredientFlexibleItem extends AbstractFlexibleItem<IngredientFlexibleItem.IngredientViewHolder> {

    private Ingredient mIngredient;

    public IngredientFlexibleItem(Ingredient ingredient){
        mIngredient = ingredient;
    }

    @Override
    public boolean equals(Object o) {
            if(o instanceof  Ingredient){
            Ingredient inIngredient = (Ingredient) o;
            return (mIngredient.getIngredientName().equals(inIngredient.getIngredientName())
            && mIngredient.getQuantity() == inIngredient.getQuantity() && mIngredient.getMeasure().equals(inIngredient.getMeasure()));
            }
            return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.ingredient_item;
        }

    @Override
    public IngredientFlexibleItem.IngredientViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new IngredientViewHolder(view, adapter, true );
        }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, IngredientFlexibleItem.IngredientViewHolder holder, int position, List<Object> payloads) {
        holder.mIngredientNameTextView.setText(mIngredient.getIngredientDescription());
        }

    public class IngredientViewHolder extends FlexibleViewHolder {
        public TextView mIngredientNameTextView;

        public IngredientViewHolder(View view, FlexibleAdapter adapter, boolean stickyHeader) {
            super(view, adapter, stickyHeader);
            mIngredientNameTextView = view.findViewById(R.id.tv_ingredient_description);
        }
    }

}
