package com.example.android.bakingbuddy;

import android.view.View;

import java.util.List;

import eu.davidea.flexibleadapter.FlexibleAdapter;
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem;
import eu.davidea.flexibleadapter.items.IFlexible;
import eu.davidea.viewholders.FlexibleViewHolder;

public class RecipeItem extends AbstractFlexibleItem<RecipeItem.RecipeViewHolder>{
    @Override
    public boolean equals(Object o) {
        return false;
    }

    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    public RecipeViewHolder createViewHolder(View view, FlexibleAdapter<IFlexible> adapter) {
        return null;
    }

    @Override
    public void bindViewHolder(FlexibleAdapter<IFlexible> adapter, RecipeViewHolder holder, int position, List<Object> payloads) {

    }

    public class RecipeViewHolder extends FlexibleViewHolder{

        public RecipeViewHolder(View view, FlexibleAdapter adapter, boolean stickyHeader) {
            super(view, adapter, stickyHeader);
        }
    }
}
