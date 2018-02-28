package com.example.recipesapp.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.example.recipesapp.R;
import com.example.recipesapp.fragments.StepDetailsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsActivity extends AppCompatActivity {
    private static final String STEP_DESC_KEY = "Desc";
    private static final String STEP_VIDEO_KEY = "Video";
    private static final String STEP_THUMB_KEY = "Thumbnail";
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String stepDescription = "";
    private String videoUrl = "";
    private String thumbnailUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getIntent().getExtras() != null) {
            stepDescription = getIntent().getExtras().getString(STEP_DESC_KEY);
            videoUrl = getIntent().getExtras().getString(STEP_VIDEO_KEY);
            thumbnailUrl = getIntent().getExtras().getString(STEP_THUMB_KEY);
        }
        if (savedInstanceState != null) {
            stepDescription = savedInstanceState.getString(STEP_DESC_KEY);
            videoUrl = savedInstanceState.getString(STEP_VIDEO_KEY);
            thumbnailUrl = savedInstanceState.getString(STEP_THUMB_KEY);
        }
        StepDetailsFragment stepFragment = (StepDetailsFragment) getSupportFragmentManager()
                .findFragmentById(R.id.step_details_frag);
        stepFragment.getStepDetails(stepDescription, videoUrl, thumbnailUrl);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STEP_DESC_KEY, stepDescription);
        outState.putString(STEP_VIDEO_KEY, videoUrl);
        outState.putString(STEP_THUMB_KEY, thumbnailUrl);
    }
}
