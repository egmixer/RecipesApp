package com.example.recipesapp.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.example.recipesapp.R;
import com.example.recipesapp.adapters.IngredientsAdapter;
import com.example.recipesapp.adapters.StepsAdapter;
import com.example.recipesapp.webservice.models.RecipeResponse;
import com.example.recipesapp.webservice.models.Step;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeStepsFragment extends Fragment {
    @BindView(R.id.ingred_rv)
    RecyclerView ingredientsList;
    @BindView(R.id.stps_rv)
    RecyclerView stepsList;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private IngredientsAdapter ingredientsAdapter;
    private StepsAdapter stepsAdapter;
    private RecipeStepHandler recipeStepHandler;
    private RecipeResponse recipeResponse;

    public void setRecipeStepHandler(RecipeStepHandler recipeStepHandler) {
        this.recipeStepHandler = recipeStepHandler;
    }

    public RecipeStepsFragment() {
        // Required empty public constructor
    }

    public void getRecipe(RecipeResponse recipeResponse) {
        this.recipeResponse = recipeResponse;
        if (recipeResponse != null) {
            ingredientsAdapter.updateList(recipeResponse.getIngredients());
            stepsAdapter.updateList(recipeResponse.getSteps());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_steps, container, false);
        ButterKnife.bind(this, rootView);
        initIngredientList();
        initStepsList();
        if (savedInstanceState == null)
            scrollView.smoothScrollTo(0, 0);
        return rootView;
    }

    private void initIngredientList() {
        ingredientsAdapter = new IngredientsAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        ingredientsList.setLayoutManager(linearLayoutManager);
        ingredientsList.setHasFixedSize(true);
        ingredientsList.setNestedScrollingEnabled(false);
        ingredientsList.setAdapter(ingredientsAdapter);
    }

    private void initStepsList() {
        stepsAdapter = new StepsAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        stepsList.setHasFixedSize(true);
        stepsList.setNestedScrollingEnabled(false);
        stepsList.setLayoutManager(linearLayoutManager);
        stepsList.setAdapter(stepsAdapter);
        stepsAdapter.setOnItemClickListener(new StepsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
                Step currentStep = recipeResponse.getSteps().get(position);
                recipeStepHandler.onItemClicked(currentStep.getDescription(), currentStep.getVideoURL(), currentStep.getThumbnailURL());
            }
        });
    }

    public interface RecipeStepHandler {
        void onItemClicked(String stepDesc, String stepUrl, String thumbUrl);
    }
}
