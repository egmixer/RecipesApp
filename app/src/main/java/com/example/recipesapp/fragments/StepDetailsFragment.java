package com.example.recipesapp.fragments;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.recipesapp.R;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepDetailsFragment extends Fragment {
    private static final String STEP_VIDEO_KEY = "Video";
    private static final String VIDEO_POSITION_KEY = "position";
    private static final String VIDEO_IS_READY_KEY = "is_ready";
    @BindView(R.id.video_player)
    SimpleExoPlayerView videoPlayer;
    @Nullable
    @BindView(R.id.step_iv)
    ImageView stepImage;
    @Nullable
    @BindView(R.id.step_desc_tv)
    TextView stepDesc;
    private SimpleExoPlayer simpleExoPlayer;
    private boolean isReady;
    private long playPosition;
    private int currentFrame;
    private String stepDescription = "";
    private String videoUrl = "";
    private String thumbnailUrl = "";

    public StepDetailsFragment() {
        // Required empty public constructor
    }

    public void getStepDetails(String desc, String videoUrl, String thumbUrl) {
        stepDescription = desc;
        this.videoUrl = videoUrl;
        thumbnailUrl = thumbUrl;
        if (stepDesc != null)
            if (!stepDescription.equals(""))
                stepDesc.setText(stepDescription);
        if (stepImage != null) {
            if (!thumbnailUrl.equals("")) {
                stepImage.setVisibility(View.VISIBLE);
                Picasso.with(getContext()).load(thumbnailUrl).into(stepImage);
            } else stepImage.setVisibility(View.GONE);
        }
        if (videoUrl.equals(""))
            videoPlayer.setVisibility(View.GONE);
        else {
            videoPlayer.setVisibility(View.VISIBLE);
            initVideoPlayer();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_step_details, container, false);
        ButterKnife.bind(this, rootView);
        if (savedInstanceState != null) {
            playPosition = savedInstanceState.getLong(VIDEO_POSITION_KEY);
            isReady = savedInstanceState.getBoolean(VIDEO_IS_READY_KEY);
            videoUrl = savedInstanceState.getString(STEP_VIDEO_KEY);
            if (getContext().getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
                if (videoUrl.equals("")) {
                    Toast.makeText(getContext(), "No Video Available", Toast.LENGTH_SHORT).show();
                } else
                    videoPlayer.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LOW_PROFILE
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        } else
            playPosition = C.TIME_UNSET;
        return rootView;
    }

    private void initVideoPlayer() {
        if (simpleExoPlayer == null) {
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                    new DefaultRenderersFactory(getContext()),
                    new DefaultTrackSelector(), new DefaultLoadControl());
            videoPlayer.setPlayer(simpleExoPlayer);
            simpleExoPlayer.setPlayWhenReady(isReady);
            simpleExoPlayer.seekTo(currentFrame, playPosition);
        }
        MediaSource mediaSource = new ExtractorMediaSource(Uri.parse(videoUrl),
                new DefaultHttpDataSourceFactory("ua"),
                new DefaultExtractorsFactory(), null, null);
        if (playPosition != C.TIME_UNSET)
            simpleExoPlayer.seekTo(playPosition);
        simpleExoPlayer.prepare(mediaSource, true, false);
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            playPosition = simpleExoPlayer.getCurrentPosition();
            currentFrame = simpleExoPlayer.getCurrentWindowIndex();
            isReady = simpleExoPlayer.getPlayWhenReady();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(STEP_VIDEO_KEY, videoUrl);
        outState.putLong(VIDEO_POSITION_KEY, playPosition);
        outState.putBoolean(VIDEO_IS_READY_KEY, isReady);
    }
}
