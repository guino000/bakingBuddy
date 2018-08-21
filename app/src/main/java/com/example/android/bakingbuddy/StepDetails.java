package com.example.android.bakingbuddy;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingbuddy.R;
import com.example.android.bakingbuddy.model.CookingStep;
import com.example.android.bakingbuddy.model.Recipe;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import net.alexandroid.utils.exoplayerhelper.ExoPlayerHelper;

import java.util.ArrayList;

public class StepDetails extends AppCompatActivity implements ExoPlayer.EventListener{
    private static final String TAG = StepDetails.class.getSimpleName();
    public static final String KEY_INTENT_CLICKED_STEP = "clicked_step";
    public static final String KEY_INTENT_STEPS_COLLECTION = "step_collection";
    public static final String KEY_STATE_CURRENT_PAGE = "current_page";
    public static final String KEY_STATE_PLAYBACK_POSITION = "playback_position";
    public static final String KEY_STATE_PLAY_WHEN_READY = "play_when_ready";
    public static final String KEY_STATE_CURRENT_WINDOW = "current_window";

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_details);

//        Set member variables
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            mStepDescriptionTextView = findViewById(R.id.tv_step_description);
            mStepShortDescriptionTextView = findViewById(R.id.tv_step_short_description);
            mButtonPrevious = findViewById(R.id.bt_previous_step);
            mButtonNext = findViewById(R.id.bt_next_step);

//        Configure Buttons
            mButtonNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCurrentPage += 1;
                    if (mCurrentPage < mSteps.size()) {
                        updateUiCurrentStep(mSteps.get(mCurrentPage));
                    }
                }
            });

            mButtonPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCurrentPage -= 1;
                    if (mCurrentPage >= 0) {
                        updateUiCurrentStep(mSteps.get(mCurrentPage));
                    }
                }
            });
            showSystemUI();
        }else{
            hideSystemUi();
        }

//        Initialize ExoPlayer and variables
        mPlayerView = findViewById(R.id.player_step_video);
        mPlayWhenReady = false;
        mCurrentWindow = 0;
        mPlaybackPosition = 0;

//        Get incoming intent
//            TODO: Configure step details activity to receive recipe ID and get all steps related to it
//            TODO: Load mSteps with steps from query
        Intent inIntent = getIntent();
        if(inIntent != null) {
            mSteps =  inIntent.getParcelableArrayListExtra(KEY_INTENT_STEPS_COLLECTION);
            mCurrentPage = inIntent.getIntExtra(KEY_INTENT_CLICKED_STEP,0);
            CookingStep currentStep = mSteps.get(mCurrentPage);
            updateUiCurrentStep(currentStep);
        }

        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(KEY_STATE_CURRENT_PAGE, mCurrentPage);
        outState.putLong(KEY_STATE_PLAYBACK_POSITION, mPlaybackPosition);
        outState.putBoolean(KEY_STATE_PLAY_WHEN_READY, mPlayWhenReady);
        outState.putInt(KEY_STATE_CURRENT_WINDOW, mCurrentWindow);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentPage = savedInstanceState.getInt(KEY_STATE_CURRENT_PAGE);
        updateUiCurrentStep(mSteps.get(mCurrentPage));

        mPlaybackPosition = savedInstanceState.getLong(KEY_STATE_PLAYBACK_POSITION);
        mPlayWhenReady = savedInstanceState.getBoolean(KEY_STATE_PLAY_WHEN_READY);
        mCurrentWindow = savedInstanceState.getInt(KEY_STATE_CURRENT_WINDOW);
        mPlayer.seekTo(mPlaybackPosition);
        mPlayer.setPlayWhenReady(mPlayWhenReady);
    }

    private void updateUiCurrentStep(CookingStep currentStep){
        mStepVideoURL = currentStep.getVideoUrl();
        if ("".equals(mStepVideoURL))
            mStepVideoURL = currentStep.getThumbURL();
        reinitializePlayer(mStepVideoURL);
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
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
        }
    }

    private void reinitializePlayer(String contentURL) {
        releasePlayer();
        mPlaybackPosition = 0;
        mCurrentWindow = 0;
        mPlayWhenReady = false;
        if(!"".equals(contentURL))
            initializePlayer(contentURL);
    }

    private void initializePlayer(String contentURL){
        mPlayer = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(this),
                new DefaultTrackSelector(), new DefaultLoadControl());

        mPlayerView.setPlayer(mPlayer);
        mPlayer.setPlayWhenReady(mPlayWhenReady);
        mPlayer.seekTo(mCurrentWindow,mPlaybackPosition);

        Uri uri = Uri.parse(contentURL);
        MediaSource mediaSource = buildMediaSource(uri);
        mPlayer.prepare(mediaSource,true,false);
        mPlayerView.getVideoSurfaceView().setVisibility(View.VISIBLE);
    }

    private MediaSource buildMediaSource(Uri uri){
        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("exoplayer-codelab")
        ).createMediaSource(uri);
    }

    //    Use for fullscreen
    private void hideSystemUi(){
        // Enables regular immersive mode.
        // For "lean back" mode, remove SYSTEM_UI_FLAG_IMMERSIVE.
        // Or for "sticky immersive," replace it with SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        // Set the content to appear under the system bars so that the
                        // content doesn't resize when the system bars hide and show.
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    // Shows the system bars by removing all the flags
    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(0);
    }

    private void releasePlayer(){
        if(mPlayer != null){
            mPlaybackPosition = mPlayer.getCurrentPosition();
            mCurrentWindow = mPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mPlayer.getPlayWhenReady();
            mPlayer.release();
            mPlayer = null;
            mPlayerView.getVideoSurfaceView().setVisibility(View.GONE);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(Util.SDK_INT > 23 && !"".equals(mStepVideoURL)){
            initializePlayer(mStepVideoURL);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
//        hideSystemUi();
        if((Util.SDK_INT <= 23 && mPlayer == null && !"".equals(mStepVideoURL))){
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
    public boolean onOptionsItemSelected(MenuItem item) {
        int selectedID = item.getItemId();

        switch (selectedID){
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                throw  new UnsupportedOperationException();
        }

        return super.onOptionsItemSelected(item);
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