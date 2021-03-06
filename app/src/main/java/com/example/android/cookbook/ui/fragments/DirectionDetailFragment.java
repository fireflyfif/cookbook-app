/*
 * PROJECT LICENSE
 *
 * This project was submitted by Iva Ivanova as part of the Nanodegree at Udacity.
 *
 * According to Udacity Honor Code we agree that we will not plagiarize (a form of cheating) the work of others. :
 * Plagiarism at Udacity can range from submitting a project you didn’t create to copying code into a program without
 * citation. Any action in which you misleadingly claim an idea or piece of work as your own when it is not constitutes
 * plagiarism.
 * Read more here: https://udacity.zendesk.com/hc/en-us/articles/360001451091-What-is-plagiarism-
 *
 * MIT License
 *
 * Copyright (c) 2018 Iva Ivanova
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.example.android.cookbook.ui.fragments;

import android.app.NotificationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.cookbook.R;
import com.example.android.cookbook.model.Step;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.RenderersFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerControlView;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DirectionDetailFragment extends Fragment implements PlayerControlView.VisibilityListener, PlaybackPreparer, Player.EventListener {

    private static final String LOG_TAG = DirectionDetailFragment.class.getSimpleName();

    // Saved instance state keys
    private static final String CLICKED_KEY = "clicked_position";
    private static final String DIRECTION_LIST_PARCEL_KEY = "direction_key";
    private static final String DIRECTION_CURRENT_KEY = "current_direction_key";
    private static final String KEY_POSITION = "position";
    private static final String KEY_WINDOW = "window";
    private static final String KEY_PLAY_WHEN_READY = "play_when_ready";

    private long mPlaybackPosition;
    private int mCurrentWindow;
    private boolean mPlayWhenReady = true;

    private Step mDirections;
    private ArrayList<Step> mDirectionList;
    private int mDirectionNumber;
    private int mPosition;


    @BindView(R.id.direction_description_tv)
    TextView mLongDescription;
    @BindView(R.id.no_video_iv)
    ImageView mNoVideoImage;
    @BindView(R.id.short_description_tv)
    TextView mShortDescription;
    @BindView(R.id.description_card_view)
    CardView mDescriptionCardView;


    // Media Player Views
    @BindView(R.id.player_view)
    PlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private String mVideoUrl;

    private boolean mVisibleToUser;

    // Mandatory empty constructor
    public DirectionDetailFragment() {}

    public static DirectionDetailFragment newInstance(ArrayList<Step> stepsList, int position) {

        DirectionDetailFragment videoFragment = new DirectionDetailFragment();

        Bundle arguments = new Bundle();
        arguments.putInt(CLICKED_KEY, position);
        arguments.putParcelableArrayList(DIRECTION_LIST_PARCEL_KEY, stepsList);
        videoFragment.setArguments(arguments);

        return videoFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mDirectionList = getArguments().getParcelableArrayList(DIRECTION_LIST_PARCEL_KEY);
            mPosition = getArguments().getInt(CLICKED_KEY);
        }

        if (savedInstanceState != null) {
            mPlaybackPosition = savedInstanceState.getLong(KEY_POSITION);
            mCurrentWindow = savedInstanceState.getInt(KEY_WINDOW);
            mPlayWhenReady = savedInstanceState.getBoolean(KEY_PLAY_WHEN_READY);

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        updateStartPosition();

        outState.putLong(KEY_POSITION, mPlaybackPosition);
        outState.putInt(KEY_WINDOW, mCurrentWindow);
        outState.putBoolean(KEY_PLAY_WHEN_READY, mPlayWhenReady);
        outState.putInt(CLICKED_KEY, mPosition);
    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_direction_detail, container, false);

        ButterKnife.bind(this, rootView);

        mDirections = mDirectionList.get(mPosition);
        if (mDirections != null) {
            mShortDescription.setText(mDirections.getShortDescription());
            mLongDescription.setText(mDirections.getDescription());
            mVideoUrl = mDirections.getVideoURL();

        }
        Log.d(LOG_TAG, "videoUrl: " + mVideoUrl);


        mPlayerView.setControllerVisibilityListener(this);
        mPlayerView.requestFocus();


        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d(LOG_TAG, "onStart is called");

        if (Util.SDK_INT > 23) {
            if (mExoPlayer != null) {
                initializePlayer(Uri.parse(mVideoUrl));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(LOG_TAG, "onResume is called");

        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            initializePlayer(Uri.parse(mVideoUrl));
        }

    }

    @Override
    public void onPause() {
        super.onPause();

        Log.d(LOG_TAG, "onPause is called");

        if (Util.SDK_INT <= 23) {
            if (mExoPlayer != null) {
                releasePlayer();
            }
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        Log.d(LOG_TAG, "onStop is called");

        if (Util.SDK_INT > 23) {
            if (mExoPlayer != null) {
                releasePlayer();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(LOG_TAG, "onDestroy is called");

        releasePlayer();
    }

    private void initializePlayer(Uri mediaUri) {

        mVisibleToUser = true;

        // Check if the video url is empty
        if (!TextUtils.isEmpty(mVideoUrl)) {
            if (mExoPlayer == null) {

                // Create an instance of the ExoPlayer
                TrackSelector trackSelector = new DefaultTrackSelector();
                LoadControl loadControl = new DefaultLoadControl();
                RenderersFactory renderersFactory = new DefaultRenderersFactory(getContext());

                // Without the loadControl (shown in the Google I/O video 2018)
                // link: https://www.youtube.com/watch?v=svdq1BWl4r8&feature=youtu.be
                mExoPlayer = ExoPlayerFactory.newSimpleInstance(renderersFactory, trackSelector, loadControl);

                // Bind the player to the PlayerView
                mPlayerView.setPlayer(mExoPlayer);

                // Set the ExoPlayer.EventListener to this activity
                mExoPlayer.addListener(this);

                DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(getActivity(),
                        Util.getUserAgent(getActivity(), "cookbook"));

                MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                        .createMediaSource(mediaUri);

                mExoPlayer.prepare(mediaSource);
                // Set the Player to play automatically
                mExoPlayer.setPlayWhenReady(mPlayWhenReady);
                mExoPlayer.seekTo(mCurrentWindow, mPlaybackPosition);

            }
        } else {
            // If the video URL is empty, check if there is a thumbnail and show it,
            // if not show the image placeholder for no video

            // Hide the video player
            mPlayerView.setVisibility(View.GONE);

            String videoThumbnailUrlString = mDirections.getThumbnailURL();

            // Check if there is a video Thumbnail Url
            if (!TextUtils.isEmpty(videoThumbnailUrlString)) {
                Picasso.get()
                        .load(mDirections.getThumbnailURL())
                        .placeholder(R.drawable.cookbook_bg_2)
                        .error(R.drawable.cookbook_bg_2)
                        .into(mNoVideoImage);
            }

            // Show the Image for no video available
            mNoVideoImage.setVisibility(View.VISIBLE);
            mNoVideoImage.setImageResource(R.drawable.cookbook_bg_2);

        }
    }

    /**
     * Release ExoPlayer
     */
    private void releasePlayer() {

        mVisibleToUser = false;

        if (mExoPlayer != null) {
            //mNotificationManager.cancelAll();

            updateStartPosition();
            // Get the current position
            mPlaybackPosition = mExoPlayer.getCurrentPosition();
            mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();

            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    private void updateStartPosition() {
        if (mExoPlayer != null) {
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
            mCurrentWindow = mExoPlayer.getCurrentWindowIndex();
            mPlaybackPosition = Math.max(0, mExoPlayer.getContentPosition());
        }
    }

    // PlaybackControlView.VisibilityListener implementation
    @Override
    public void onVisibilityChange(int visibility) {

    }

    // Player Event Listeners
    @Override
    public void preparePlayback() {

        initializePlayer(Uri.parse(mVideoUrl));
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

    /**
     * Method that helps the exo player to be stopped when a tab is being changed/swiped from the ViewPager
     *
     * @param isVisibleToUser that indicates whether the item is visible to the user or not
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mVisibleToUser = isVisibleToUser;
        if (isVisibleToUser) {
            Log.d(LOG_TAG, "Visible to the user ");

            if (!TextUtils.isEmpty(mVideoUrl)) {
                initializePlayer(Uri.parse(mVideoUrl));
            }

        } else {
            Log.d(LOG_TAG, "Not visible to the user ");
            releasePlayer();
        }
    }
}
