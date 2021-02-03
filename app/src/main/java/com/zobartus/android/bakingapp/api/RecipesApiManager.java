package com.zobartus.android.bakingapp.api;

import com.zobartus.android.bakingapp.model.Recipes;
import com.zobartus.android.bakingapp.utils.Constants;

import java.io.Serializable;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RecipesApiManager implements Serializable{
    private static volatile  RecipesApiManager sharedInstance =
            new RecipesApiManager();
    private RecipesApiService service;

    private RecipesApiManager(){
        if (sharedInstance != null) {
            throw new RuntimeException("Stuff");
        }

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.RECIPES_API_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        service = retrofit.create(RecipesApiService.class);
    }

    public static RecipesApiManager getInstance() {
        if (sharedInstance == null) {
            synchronized (RecipesApiManager.class) {
                if (sharedInstance == null) sharedInstance = new RecipesApiManager();
            }
        }

        return sharedInstance;
    }

    public void getRecipes(final RecipesApiCallback<List<Recipes>> recipesApiCallback) {
        service.getRecipes().enqueue(new Callback<List<Recipes>>() {
            @Override
            public void onResponse(Call<List<Recipes>> call, Response<List<Recipes>> response) {
                recipesApiCallback.onResponse(response.body());
            }

            @Override
            public void onFailure(Call<List<Recipes>> call, Throwable t) {
                if (call.isCanceled()) {
                    recipesApiCallback.onCancel();
                } else {
                    recipesApiCallback.onResponse(null);
                }
            }
        });
    }
}
