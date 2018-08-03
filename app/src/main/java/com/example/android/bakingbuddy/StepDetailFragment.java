package com.example.android.bakingbuddy;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingbuddy.model.CookingStep;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.ArrayList;

public class StepDetailFragment extends Fragment implements Player.EventListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM_CLICKED_STEP = "clicked_step";
    private static final String ARG_PARAM_STEPS_COLLECTION = "step_collection";
    public static final String KEY_STATE_CURRENT_PAGE = "current_page";
    public static final String KEY_STATE_PLAYBACK_POSITION = "playback_position";
    public static final String KEY_STATE_PLAY_WHEN_READY = "play_when_ready";
    public static final String KEY_STATE_CURRENT_WINDOW = "current_window";

    // TODO: Rename and change types of parameters
    private int mCurrentPage;
    private ArrayList<CookingStep> mSteps;
    private TextView mStepDescriptionTextView;
    private TextView mStepShortDescriptionTextView;
    private PlayerView mPlayerView;
    private SimpleExoPlayer mPlayer;
    private long mPlaybackPosition;
    private int mCurrentWindow;
    private boolean mPlayWhenReady;
    private String mStepVideoURL;
    private Button mButtonPrevious;
    private Button mButtonNext;

    public StepDetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param cookingSteps Parameter 1.
     * @param clickedStep Parame0ter 2.
     * @return A new instance of fragment StepDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static StepDetailFragment newInstance(ArrayList<CookingStep> cookingSteps, int clickedStep) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(ARG_PARAM_STEPS_COLLECTION, cookingSteps);
        args.putInt(ARG_PARAM_CLICKED_STEP, clickedStep);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mSteps = getArguments().getParcelableArrayList(ARG_PARAM_STEPS_COLLECTION);
            mCurrentPage = getArguments().getInt(ARG_PARAM_CLICKED_STEP);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        Set member variables
        mStepDescriptionTextView = view.findViewById(R.id.tv_step_description);
        mStepShortDescriptionTextView = view.findViewById(R.id.tv_step_short_description);
        mButtonPrevious = view.findViewById(R.id.bt_previous_step);
        mButtonNext = view.findViewById(R.id.bt_next_step);

//        Configure Buttons
        mButtonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPage += 1;
                if (mCurrentPage < mSteps.size()) {
                    updateUiCurrentStep(mCurrentPage);
                }
            }
        });

        mButtonPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPage -= 1;
                if (mCurrentPage >= 0) {
                    updateUiCurrentStep(mCurrentPage);
                }
            }
        });

        //        Initialize ExoPlayer and variables
        mPlayerView = view.findViewById(R.id.player_step_video);
        mPlayWhenReady = false;
        mCurrentWindow = 0;
        mPlaybackPosition = 0;
        updateUiCurrentStep(mCurrentPage);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(KEY_STATE_CURRENT_PAGE, mCurrentPage);
        if(mPlayer!= null) {
            outState.putLong(KEY_STATE_PLAYBACK_POSITION, mPlayer.getCurrentPosition());
            outState.putBoolean(KEY_STATE_PLAY_WHEN_READY, mPlayer.getPlayWhenReady());
            outState.putInt(KEY_STATE_CURRENT_WINDOW, mPlayer.getCurrentWindowIndex());
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState == null)
            return;

        mCurrentPage = savedInstanceState.getInt(KEY_STATE_CURRENT_PAGE);
        updateUiCurrentStep(mCurrentPage);

        mPlaybackPosition = savedInstanceState.getLong(KEY_STATE_PLAYBACK_POSITION,0);
        mPlayWhenReady = savedInstanceState.getBoolean(KEY_STATE_PLAY_WHEN_READY, false);
        mCurrentWindow = savedInstanceState.getInt(KEY_STATE_CURRENT_WINDOW, 0);
//        TODO: Check why the seekto command is not working
        if (mPlayer != null) {
            mPlayer.seekTo(mCurrentWindow, mPlaybackPosition);
            mPlayer.setPlayWhenReady(mPlayWhenReady);
        }
    }

    public void updateUiCurrentStep(int page){
        CookingStep currentStep = mSteps.get(page);
        mStepVideoURL = currentStep.getVideoUrl();
        if ("".equals(mStepVideoURL))
            mStepVideoURL = currentStep.getThumbURL();
        if(!"".equals(mStepVideoURL))
            reinitializePlayer(mStepVideoURL);
        mStepDescriptionTextView.setText(currentStep.getDescription());
        mStepShortDescriptionTextView.setText(currentStep.getShortDescription());
        if (mCurrentPage == mSteps.size() - 1)
            mButtonNext.setVisibility(View.INVISIBLE);
        else if (mCurrentPage == 0)
            mButtonPrevious.setVisibility(View.INVISIBLE);
        else {
            mButtonPrevious.setVisibility(View.VISIBLE);
            mButtonNext.setVisibility(View.VISIBLE);
        }
        mCurrentPage = page;
    }

    private void reinitializePlayer(String contentURL) {
        releasePlayer();
        mPlaybackPosition = 0;
        mCurrentWindow = 0;
        mPlayWhenReady = false;
        initializePlayer(contentURL);
    }

    private void initializePlayer(String contentURL){
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(getContext()),
                new DefaultTrackSelector(), new DefaultLoadControl());

        mPlayerView.setPlayer(mPlayer);
        mPlayer.setPlayWhenReady(mPlayWhenReady);
        mPlayer.seekTo(mCurrentWindow,mPlaybackPosition);

        Uri uri = Uri.parse(contentURL);
        MediaSource mediaSource = buildMediaSource(uri);
        mPlayer.prepare(mediaSource,true,false);
    }

    private MediaSource buildMediaSource(Uri uri){
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")
        ).createMediaSource(uri);
    }

    private void releasePlayer(){
        if(mPlayer != null){
            mPlaybackPosition = mPlayer.getCurrentPosition();
            mCurrentWindow = mPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if(Util.SDK_INT > 23 && !"".equals(mStepVideoURL)){
            initializePlayer(mStepVideoURL);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//        hideSystemUi();
        if((Util.SDK_INT <= 23 && !"".equals(mStepVideoURL))){
            initializePlayer(mStepVideoURL);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }
}
