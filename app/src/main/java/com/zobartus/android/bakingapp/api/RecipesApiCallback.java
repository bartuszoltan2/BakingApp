package com.zobartus.android.bakingapp.api;

public interface RecipesApiCallback<T> {

    void onResponse(T result);
    void onCancel();
}
