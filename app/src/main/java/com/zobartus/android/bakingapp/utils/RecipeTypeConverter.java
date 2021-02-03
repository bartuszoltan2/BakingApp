package com.zobartus.android.bakingapp.utils;

import android.arch.persistence.room.TypeConverter;

import com.zobartus.android.bakingapp.model.Ingredients;

import java.util.List;

public class RecipeTypeConverter {
    @TypeConverter
    public static Ingredients listToIngredients(List<Ingredients> ingredients){
        Ingredients ingredients1;

        for (int i = 0; i < ingredients.size(); i++) {

        }
        return null;
    }
}
