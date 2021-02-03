package com.zobartus.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.zobartus.android.bakingapp.utils.Prefs;
import com.zobartus.android.bakingapp.R;
import com.zobartus.android.bakingapp.model.Recipes;
import com.zobartus.android.bakingapp.ui.activity.MainActivity;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {

    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {
        RemoteViews views;
        Recipes recipes = Prefs.loadRecipe(context);
        if (recipes != null) {
            // TODO: 7/4/2018 Make each click go to each specific detail screen 
            // TODO: 7/6/2018 Learn how to use the button in widget 
            PendingIntent intent = PendingIntent.getActivity(context, 0,
                    new Intent(context, MainActivity.class), 0);
            // Construct the RemoteViews object
            views = new RemoteViews(context.getPackageName(), R.layout.baking_app_widget);
            views.setTextViewText(R.id.appwidget_text, recipes.getName());

            views.setOnClickPendingIntent(R.id.appwidget_text, intent);

            //Button click
//            PendingIntent previous_intent = PendingIntent.getActivity(context, 1, )
//            views.setOnClickPendingIntent(R.id.previous_button, previous_intent);

            Intent intent1 = new Intent(context, WidgetService.class);
            intent1.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            views.setRemoteAdapter(R.id.recipe_step_list_view, intent1);
            appWidgetManager.updateAppWidget(appWidgetId, views);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.recipe_step_list_view);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    public static void onUpdateWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

