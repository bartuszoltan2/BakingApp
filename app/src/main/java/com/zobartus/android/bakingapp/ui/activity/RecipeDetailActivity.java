package com.zobartus.android.bakingapp.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.zobartus.android.bakingapp.R;
import com.zobartus.android.bakingapp.adapter.RecipeDetailAdapter;
import com.zobartus.android.bakingapp.model.Recipes;
import com.zobartus.android.bakingapp.ui.fragment.RecipeStepsFragment;
import com.zobartus.android.bakingapp.widget.WidgetService;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailActivity extends AppCompatActivity {

    public static final String RECIPE_KEY = "recipes";

    @BindView(R.id.recipe_step_list_recycler)
    RecyclerView recyclerView;
    @BindView(R.id.detail_toolbar)
    Toolbar toolbar;

    private Recipes recipes;
    private boolean twoPane;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        ButterKnife.bind(this);


        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(RECIPE_KEY)){
            recipes = getIntent().getExtras().getParcelable(RECIPE_KEY);
        } else {
            Snackbar.make(Objects.requireNonNull(getCurrentFocus()), getResources().getText(R.string.noData), Snackbar.LENGTH_LONG).show();
        }

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(recipes.getName());
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        
        twoPane = getResources().getBoolean(R.bool.twoPaneMode);
        if (twoPane){
            if (savedInstanceState == null && !recipes.getSteps().isEmpty()) {
                openStepDetail(0);
            }
        }
        initView();
    }

    private void initView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecipeDetailAdapter(recipes, this::openStepDetail));
    }


    private void openStepDetail(int position) {
        if (twoPane){
            Bundle arguments = new Bundle();
            arguments.putParcelable(RecipeStepsDetail.STEP_SELECTED_KEY, recipes.getSteps().get(position));
            RecipeStepsFragment fragment = new RecipeStepsFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.recipe_step_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, RecipeStepsDetail.class);
            intent.putExtra(RECIPE_KEY, recipes);
            intent.putExtra(RecipeStepsDetail.STEP_SELECTED_KEY, position);
            startActivity(intent);
        }
        
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater()
                .inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.add_widget) {
            WidgetService.addWidget(this, recipes);
            Snackbar.make(Objects.requireNonNull(getCurrentFocus()), getResources().getText(R.string.addedHomeScreen), Snackbar.LENGTH_LONG).show();
            return true;
        } else{
            return super.onOptionsItemSelected(item);
        }
    }
}
