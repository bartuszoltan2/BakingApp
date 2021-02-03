package com.zobartus.android.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViewsService;

import com.zobartus.android.bakingapp.utils.Prefs;
import com.zobartus.android.bakingapp.model.Recipes;

public class WidgetService extends RemoteViewsService {

    public static void addWidget(Context context, Recipes recipes){
        Prefs.addRecipe(context, recipes);

        AppWidgetManager manager = AppWidgetManager.getInstance(context);
        int[] widgetsIds = manager.getAppWidgetIds(new ComponentName(context, BakingAppWidget.class));
        BakingAppWidget.onUpdateWidgets(context, manager, widgetsIds);
    }
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        intent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        return new WidgetRemoteViewFactory(getApplicationContext());
    }
}
