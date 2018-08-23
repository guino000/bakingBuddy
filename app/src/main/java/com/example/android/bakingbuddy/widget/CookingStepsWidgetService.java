package com.example.android.bakingbuddy.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.widget.ListView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.bakingbuddy.R;
import com.example.android.bakingbuddy.model.CookingStep;

import java.util.ArrayList;
import java.util.List;

public class CookingStepsWidgetService extends RemoteViewsService{
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new CookingStepsRemoteViewsFactory(this.getApplicationContext(), intent);
    }

    class CookingStepsRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory{
        private Context mContext;
        private List<CookingStep> mCookingSteps = new ArrayList<>();
        private int mAppWidgetId;

        public CookingStepsRemoteViewsFactory(Context context, Intent intent){
            mContext = context;
            mAppWidgetId = intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        @Override
        public void onCreate() {
//            TODO: Implement a way of getting the data from source
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {

        }

        @Override
        public int getCount() {
            return mCookingSteps.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.widget_cooking_step_item);
            rv.setTextViewText(R.id.tv_widget_step_description, mCookingSteps.get(position).getDescription());
            return rv;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 0;
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
