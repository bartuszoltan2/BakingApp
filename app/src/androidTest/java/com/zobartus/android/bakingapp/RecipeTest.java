package com.zobartus.android.bakingapp;

import android.content.Context;
import android.content.Intent;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.intent.Intents;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import android.support.test.runner.AndroidJUnit4;

import com.zobartus.android.bakingapp.model.Recipes;
import com.zobartus.android.bakingapp.ui.activity.RecipeDetailActivity;
import com.zobartus.android.bakingapp.ui.activity.RecipeStepsDetail;
import com.zobartus.android.bakingapp.utils.Prefs;

import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.isCompletelyDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;

@RunWith(AndroidJUnit4.class)
public class RecipeTest  extends BaseTest{

    public static void getMeToRecipeInfo(int recipePosition) {
        onView(withId(R.id.main_recipes_recyclerview))
                .perform(RecyclerViewActions.actionOnItemAtPosition(recipePosition, click()));
    }

    public static void selectRecipeStep(int recipeStep) {
        onView(withId(R.id.recipe_step_list_recycler))
                .perform(RecyclerViewActions.actionOnItemAtPosition(recipeStep, click()));
    }

    @Test
    public void clickRecyclerITemWithKey(){
        Intents.init();
        getMeToRecipeInfo(0);

        intended(hasExtraWithKey(RecipeDetailActivity.RECIPE_KEY));
        Intents.release();
    }

    @Test
    public void clickOnRecyclerViewItem_opensRecipeInfoActivity() {

        getMeToRecipeInfo(0);

        onView(withId(R.id.ingredients_txt))
                .check(matches(isDisplayed()));

        onView(withId(R.id.recipe_step_list_recycler))
                .check(matches(isDisplayed()));
    }

    @Test
    public void clickOnRecyclerViewStepItem_opensRecipeStepActivity_orFragment() {
        getMeToRecipeInfo(0);

        boolean twoPaneMode = baseApplication.getResources().getBoolean(R.bool.twoPaneMode);
        if (!twoPaneMode) {
            // Checks if the keys are present and the intent launched is RecipeStepDetailActivity
            Intents.init();
            selectRecipeStep(1);
            intended(hasComponent(RecipeStepsDetail.class.getName()));
            intended(hasExtraWithKey(RecipeDetailActivity.RECIPE_KEY));
            intended(hasExtraWithKey(RecipeStepsDetail.STEP_SELECTED_KEY));
            Intents.release();

            // Check TabLayout
            onView(withId(R.id.tabs))
                    .check(matches(isCompletelyDisplayed()));
        } else {
            selectRecipeStep(1);

            onView(withId(R.id.exoplayer_view))
                    .check(matches(isDisplayed()));
        }
    }

    @Test
    public void checkAddWidgetButtonFunctionality() {
        // Clear the preferences values
        baseApplication.getSharedPreferences(Prefs.PREF, Context.MODE_PRIVATE).edit()
                .clear()
                .commit();

        getMeToRecipeInfo(0);

        onView(withId(R.id.add_widget))
                .check(matches(isDisplayed()))
                .perform(click());

        // Get the recipe base64 string from the sharedPrefs
        Recipes recipe = Prefs.loadRecipe(baseApplication);

        // Assert recipe is not null
        assertNotNull(recipe);
    }
}
