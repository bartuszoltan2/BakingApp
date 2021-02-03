package com.zobartus.android.bakingapp.widget;

import android.content.Context;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.zobartus.android.bakingapp.utils.Prefs;
import com.zobartus.android.bakingapp.R;
import com.zobartus.android.bakingapp.model.Recipes;

public class WidgetRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context context;
    private Recipes recipes;

    public WidgetRemoteViewFactory(Context context) {
        this.context = context;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        //TODO: Fix this
        recipes = Prefs.loadRecipe(context);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return recipes.getIngredients().size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget_list_item);
        views.setTextViewText(R.id.widget_ingredient_txt, recipes.getIngredients().get(i).getIngredient());
        return views;
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
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
