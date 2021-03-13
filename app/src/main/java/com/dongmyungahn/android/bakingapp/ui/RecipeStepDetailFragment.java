package com.dongmyungahn.android.bakingapp.ui;

import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dongmyungahn.android.bakingapp.R;
import com.dongmyungahn.android.bakingapp.model.RecipeStep;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class RecipeStepDetailFragment extends Fragment {

    private static final String SAVED_INSTANCE_EXO_PLAYER_POS = "saved_instance_exo_player_pos";

    @Nullable @BindView(R.id.simple_exo_play_view) SimpleExoPlayerView mSimpleExoPlayerView;
    @Nullable @BindView(R.id.tv_recipe_step_short_description) TextView mTvRecipeStepShortDescription;
    @Nullable @BindView(R.id.tv_recipe_step_description) TextView mTvRecipeStepDescription;

    private RecipeStep mStep;

    private SimpleExoPlayer mExoPlayer;
    private long mExoPlayerCurrentPosition;

    private Unbinder mUnbinder;

    private boolean mIsTablet;
    private boolean mIsLandscape;

    private int mId;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private String mShortDescription;
    private String mDescription;

    public RecipeStepDetailFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mIsTablet = getResources().getBoolean(R.bool.isTablet);
        mIsLandscape = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);

        mUnbinder = ButterKnife.bind(this, rootView);

        if(savedInstanceState != null) {
            mExoPlayerCurrentPosition = savedInstanceState.getLong(SAVED_INSTANCE_EXO_PLAYER_POS);
        } else {
            mExoPlayerCurrentPosition = 0;
        }

        mId = mStep.getId();
        mShortDescription = mStep.getShortDescription();
        mDescription = mStep.getDescription();
        mVideoUrl = mStep.getVideoURL();
        mThumbnailUrl = mStep.getThumbnailURL();

        if(mVideoUrl != null && !mVideoUrl.equals((""))) {
            mSimpleExoPlayerView.setVisibility(View.VISIBLE);
            initializeExoPlayer(Uri.parse(mVideoUrl));
        } else {
            mSimpleExoPlayerView.setVisibility(View.GONE);
        }

        if(mShortDescription != null && !mShortDescription.equals("")) {
            mTvRecipeStepShortDescription.setText(mShortDescription);
        }
        if(mDescription != null && !mDescription.equals("") && !mDescription.equals(mShortDescription)) {
            mTvRecipeStepDescription.setText(mDescription);
        }

        return rootView;
    }

    public void setStep(RecipeStep step) {
        mStep = step;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong(SAVED_INSTANCE_EXO_PLAYER_POS,mExoPlayer.getCurrentPosition());
    }

    @Override
    public void onStart() {
        super.onStart();
        initializeExoPlayer(Uri.parse(mVideoUrl));
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mExoPlayer != null) {
            mExoPlayerCurrentPosition = mExoPlayer.getCurrentPosition();
            releaseExoPlayer();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
        releaseExoPlayer();
    }

    private void initializeExoPlayer(Uri mediaUri){
        if(mExoPlayer == null){
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(),trackSelector,loadControl);
            mSimpleExoPlayerView.setPlayer(mExoPlayer);
            String userAgent = Util.getUserAgent(getContext(),"BackingApp");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(),userAgent), new DefaultExtractorsFactory(),null,null);
            mExoPlayer.prepare(mediaSource);
            if(mExoPlayerCurrentPosition > 0) {
                mExoPlayer.seekTo(mExoPlayerCurrentPosition);
            }
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releaseExoPlayer(){
        if(mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

}
