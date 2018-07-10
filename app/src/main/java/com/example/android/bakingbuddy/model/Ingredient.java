package com.example.android.bakingbuddy.model;

import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakingbuddy.R;

import org.w3c.dom.Text;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;

public class Ingredient extends AbstractFlexibleItem<Ingredient.IngredientViewHolder> {
    private int mQuantity;
    private String mMeasure;
    private String mIngredientName;

    public Ingredient(int quantity, String measure, String ingredientName){
        mQuantity = quantity;
        mMeasure = measure;
        mIngredientName = ingredientName;
    }

    public int getQuantity() {
        return mQuantity;
    }

    public String getMeasure() {
        return mMeasure;
    }

    public String getIngredientName() {
        return mIngredientName;
    }

    public String getIngredientDescription(){
        return String.format("%s %s %s", String.valueOf(mQuantity), mMeasure, mIngredientName);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof  Ingredient){
            Ingredient inIngredient = (Ingredient) o;
            return (mIngredientName.equals(inIngredient.getIngredientName())
                && mQuantity == inIngredient.getQuantity() && mMeasure.equals(inIngredient.getMeasure()));
        }
        return false;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.ingredient_item;
    }

    @Override
    public Ingredient.IngredientViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return new IngredientViewHolder(view, adapter, true );
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, Ingredient.IngredientViewHolder holder, int position, List<Object> payloads) {
        holder.mIngredientNameTextView.setText(this.getIngredientDescription());
    }

    public class IngredientViewHolder extends FlexibleViewHolder{
        public TextView mIngredientNameTextView;

        public IngredientViewHolder(View view, FlexibleAdapter adapter, boolean stickyHeader) {
            super(view, adapter, stickyHeader);
            mIngredientNameTextView = view.findViewById(R.id.tv_ingredient_description);
        }
    }
}
