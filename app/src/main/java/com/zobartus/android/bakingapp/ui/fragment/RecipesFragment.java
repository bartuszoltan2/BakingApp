package com.zobartus.android.bakingapp.ui.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zobartus.android.bakingapp.BaseApplication;
import com.zobartus.android.bakingapp.R;
import com.zobartus.android.bakingapp.adapter.RecipesAdapter;
import com.zobartus.android.bakingapp.api.RecipesApiCallback;
import com.zobartus.android.bakingapp.api.RecipesApiManager;
import com.zobartus.android.bakingapp.model.Recipes;
import com.zobartus.android.bakingapp.ui.activity.RecipeDetailActivity;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipesFragment extends Fragment {

    @BindView(R.id.main_recipes_recyclerview)
    RecyclerView recyclerView;
    @BindView(R.id.pull_to_refresh)
    SwipeRefreshLayout refreshLayout;

    private List<Recipes> recipesList;

    private BaseApplication baseApplication;

    private BroadcastReceiver networkChange = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (recipesList == null) {
                loadRecipes();
            }
        }
    };

    private static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return Objects.requireNonNull(cm).getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recipes_list, container, false);
        ButterKnife.bind(this, view);

        refreshLayout.setOnRefreshListener(this::loadRecipes);
        baseApplication = (BaseApplication)getActivity().getApplicationContext();
        baseApplication.setIdleState(false);
        initView();
        return view;
    }

    private void initView() {
        recyclerView.setHasFixedSize(true);

        boolean twoPane = getResources().getBoolean(R.bool.twoPaneMode);
        if (twoPane){
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        } else{
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        }
        recyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener());
    }

    private void loadRecipes(){
        if (isConnected(Objects.requireNonNull(getContext()))){
            refreshLayout.setRefreshing(true);
            RecipesApiManager.getInstance().getRecipes(new RecipesApiCallback<List<Recipes>>() {
                @Override
                public void onResponse(List<Recipes> result) {
                    if (result != null) {
                        refreshLayout.setRefreshing(false);
                        recipesList = result;
                        recyclerView.setAdapter(new RecipesAdapter(getContext(), recipesList, position -> {
                            Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
                            intent.putExtra(RecipeDetailActivity.RECIPE_KEY, recipesList.get(position));
                            startActivity(intent);
                        }));
                        baseApplication.setIdleState(true);
                    }
                }

                @Override
                public void onCancel() {
                    Snackbar.make(Objects.requireNonNull(getView()), getResources().getText(R.string.request_cancel), Snackbar.LENGTH_LONG).show();
                }
            });
        } else {
            Snackbar.make(Objects.requireNonNull(getView()), getResources().getText(R.string.no_connection), Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Objects.requireNonNull(getActivity()).registerReceiver(networkChange, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void onPause() {
        super.onPause();
        Objects.requireNonNull(getActivity()).unregisterReceiver(networkChange);
    }
}
