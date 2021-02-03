package com.zobartus.android.bakingapp.utils;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.zobartus.android.bakingapp.R;
import com.zobartus.android.bakingapp.model.Step;
import com.zobartus.android.bakingapp.ui.activity.RecipeStepsDetail;
import com.zobartus.android.bakingapp.ui.fragment.RecipeStepsFragment;

import java.util.List;

public class StepsPagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private List<Step> steps;

    public StepsPagerAdapter(Context context, List<Step> steps, FragmentManager fm) {
        super(fm);
        this.context = context;
        this.steps = steps;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(RecipeStepsDetail.STEP_SELECTED_KEY, steps.get(position));
        RecipeStepsFragment fragment = new RecipeStepsFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        // TODO: 7/2/2018 FIx the title and Corresponding tab
        return String.format(String.valueOf(context.getResources().getText(R.string.step_title)), position + 1);
    }

    @Override

    public int getCount() {
        return steps.size();
    }
}
