package com.example.recipesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.recipesapp.R;
import com.example.recipesapp.SimpleIdlingResource;
import com.example.recipesapp.adapters.RecipesAdapter;
import com.example.recipesapp.utils.ConnectivityReceiver;
import com.example.recipesapp.webservice.WebService;
import com.example.recipesapp.webservice.models.RecipeResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private static final String RECIPE_KEY = "recipe";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.recipes_rv)
    RecyclerView recipesList;
    private RecipesAdapter recipesAdapter;
    private ArrayList<RecipeResponse> recipeResponses = new ArrayList<>();
    private MaterialDialog dialog;
    private boolean isConnected;
    private SimpleIdlingResource simpleIdlingResource = new SimpleIdlingResource();

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (simpleIdlingResource == null) {
            simpleIdlingResource = new SimpleIdlingResource();
        }
        return simpleIdlingResource;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        isConnected = ConnectivityReceiver.isConnected(this);
        setRecipesList();
        if (savedInstanceState == null)
            getRecipes();
        else {
            recipeResponses = savedInstanceState.getParcelableArrayList(RECIPE_KEY);
            recipesAdapter.updateList(recipeResponses);
            if (simpleIdlingResource != null) {
                simpleIdlingResource.setIdleState(true);
            }
        }

    }

    private void setRecipesList() {
        recipesList.setHasFixedSize(true);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        recipesList.setLayoutManager(mLayoutManager);
        recipesList.setHasFixedSize(true);
        recipesAdapter = new RecipesAdapter(recipeResponses, this);
        recipesList.setAdapter(recipesAdapter);
        recipesAdapter.setOnItemClickListener(new RecipesAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                RecipeResponse currentRecipe = recipeResponses.get(position);
                Intent intent = new Intent(MainActivity.this, StepsActivity.class);
                intent.putExtra(RECIPE_KEY, currentRecipe);
                startActivity(intent);
            }
        });
    }

    private void getRecipes() {
        if (isConnected) {
            showLoading();
            recipeResponses.clear();
            WebService webService = WebService.getInstance();
            Call<List<RecipeResponse>> getRecipes = webService.getServices().getRecipes();
            getRecipes.enqueue(new Callback<List<RecipeResponse>>() {
                @Override
                public void onResponse(@NonNull Call<List<RecipeResponse>> call, @NonNull final Response<List<RecipeResponse>> response) {
                    dialog.dismiss();
                    if (response.isSuccessful()) {
                        recipeResponses.addAll(response.body());
                        recipesAdapter.updateList(recipeResponses);
                        if (simpleIdlingResource != null) {
                            simpleIdlingResource.setIdleState(true);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<RecipeResponse>> call, @NonNull Throwable t) {
                    dialog.dismiss();
                }
            });
        } else
            Toast.makeText(this, R.string.internet_error, Toast.LENGTH_LONG).show();
    }

    private void showLoading() {
        if (dialog != null && dialog.isShowing())
            dialog.dismiss();
        dialog = new MaterialDialog.Builder(MainActivity.this)
                .title(R.string.loading)
                .content(R.string.wait)
                .progress(true, 0)
                .cancelable(false)
                .show();
        if (simpleIdlingResource != null) {
            simpleIdlingResource.setIdleState(false);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(RECIPE_KEY, recipeResponses);
    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        this.isConnected = isConnected;
    }
}
