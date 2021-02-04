package com.zobartus.android.bakingapp;

import android.app.Application;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;

import com.squareup.picasso.BuildConfig;
import com.zobartus.android.bakingapp.idlingResources.RecipeIdlingResources;

public class BaseApplication extends Application {

    private RecipeIdlingResources idlingResources;

    @VisibleForTesting
    private IdlingResource initialize() {
        if (idlingResources == null) {
            idlingResources = new RecipeIdlingResources();
        }
        return idlingResources;
    }

    public BaseApplication() {
        if (BuildConfig.DEBUG) {
            initialize();
        }
    }

    public void setIdleState(boolean state) {
        if (idlingResources != null) {
            idlingResources.setIdleState(state);
        }

        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(() -> {

            if (idlingResources != null) {
                idlingResources.setIdleState(true);
            }

        }, 5000);

    }

    public RecipeIdlingResources getIdlingResources() {
        return idlingResources;
    }
}
