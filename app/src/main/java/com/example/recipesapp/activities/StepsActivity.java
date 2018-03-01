package com.example.recipesapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.recipesapp.R;
import com.example.recipesapp.fragments.RecipeStepsFragment;
import com.example.recipesapp.fragments.StepDetailsFragment;
import com.example.recipesapp.webservice.models.RecipeResponse;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsActivity extends AppCompatActivity implements RecipeStepsFragment.RecipeStepHandler {
    private static final String RECIPE_KEY = "recipe";
    private static final String STEP_DESC_KEY = "Desc";
    private static final String STEP_VIDEO_KEY = "Video";
    private static final String STEP_THUMB_KEY = "Thumbnail";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private RecipeResponse recipeResponse;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_steps);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        if (getIntent() != null) {
            recipeResponse = getIntent().getParcelableExtra(RECIPE_KEY);
        }
        if (savedInstanceState != null)
            recipeResponse = savedInstanceState.getParcelable(RECIPE_KEY);
        RecipeStepsFragment recipeStepsFragment = (RecipeStepsFragment)
                getSupportFragmentManager().findFragmentById(R.id.recipe_frag);
        recipeStepsFragment.setRecipeStepHandler(this);
        recipeStepsFragment.getRecipe(recipeResponse);
    }

    @Override
    public void onItemClicked(String stepDesc, String stepUrl, String thumbUrl) {
        StepDetailsFragment stepDetailsFragment = (StepDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.step_details_frag);
        if (stepDetailsFragment == null) {
            Intent intent = new Intent(this, StepDetailsActivity.class);
            intent.putExtra(STEP_DESC_KEY, stepDesc);
            intent.putExtra(STEP_VIDEO_KEY, stepUrl);
            intent.putExtra(STEP_THUMB_KEY, thumbUrl);
            startActivity(intent);
        } else
            stepDetailsFragment.getStepDetails(stepDesc, stepUrl, thumbUrl);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(RECIPE_KEY, recipeResponse);
    }
}
