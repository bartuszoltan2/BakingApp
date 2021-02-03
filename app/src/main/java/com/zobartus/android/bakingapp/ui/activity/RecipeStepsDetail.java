package com.zobartus.android.bakingapp.ui.activity;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;


import com.zobartus.android.bakingapp.R;
import com.zobartus.android.bakingapp.model.Recipes;
import com.zobartus.android.bakingapp.utils.StepsPagerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsDetail extends AppCompatActivity {

    @BindView(R.id.step_detail_toolbar)
    Toolbar toolbar;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.main_viewpager)
    ViewPager pager;

    private Recipes recipes;
    private int selectedPosition;

    public static final String STEP_SELECTED_KEY = "step";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps_detail);
        ButterKnife.bind(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.containsKey(RecipeDetailActivity.RECIPE_KEY) && bundle.containsKey(STEP_SELECTED_KEY)){
            recipes = bundle.getParcelable(RecipeDetailActivity.RECIPE_KEY);
            selectedPosition = bundle.getInt(STEP_SELECTED_KEY);
        }
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(recipes.getName());
           getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        StepsPagerAdapter pagerAdapter = new StepsPagerAdapter(getApplicationContext(),
                recipes.getSteps(), getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (getSupportActionBar() != null){
                    getSupportActionBar().setTitle(recipes.getSteps().get(position).getShortDescription());
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        pager.setCurrentItem(selectedPosition);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
