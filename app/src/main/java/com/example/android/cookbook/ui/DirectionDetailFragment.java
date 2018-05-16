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

package com.example.android.cookbook.ui;

import android.app.NotificationManager;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.google.android.exoplayer2.ExoPlayer;
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

    private static final String RECIPE_PARCEL_KEY = "recipe_key";
    private static final String DIRECTION_PARCEL_KEY = "direction_key";
    private static final String DIRECTION_CURRENT_KEY = "current_direction_key";
    private static final String LOG_TAG = DirectionDetailFragment.class.getSimpleName();

    private Step mDirections;
    private ArrayList<Step> mDirectionList;

    @BindView(R.id.direction_description_tv)
    TextView mLongDescription;
    @BindView(R.id.no_video_iv)
    ImageView mNoVideoImage;
    @BindView(R.id.short_description_tv)
    TextView mShortDescription;

    // Media Player Views
    @BindView(R.id.player_view)
    PlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private NotificationManager mNotificationManager;
    private String mVideoUrl;

    // Mandatory empty constructor
    public DirectionDetailFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_direction_detail, container, false);

        ButterKnife.bind(this, rootView);

        if (getActivity().getIntent().getExtras() != null) {
            Bundle bundle = getActivity().getIntent().getExtras();
            mDirectionList = bundle.getParcelableArrayList(DIRECTION_PARCEL_KEY);
            mDirections = bundle.getParcelable(DIRECTION_CURRENT_KEY);

            if (mDirections != null) {
                mShortDescription.setText(mDirections.getShortDescription());
                mLongDescription.setText(mDirections.getDescription());
                mVideoUrl = mDirections.getVideoURL();
            }
            Log.d(LOG_TAG, "videoUrl: " + mVideoUrl);

        }

        mPlayerView.setControllerVisibilityListener(this);
        mPlayerView.requestFocus();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if (Util.SDK_INT > 23) {
            if (mVideoUrl != null) {
                initializePlayer(Uri.parse(mVideoUrl));
            } else {
                // Load default image as a background image if there is no video
                mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(
                        getResources(), R.drawable.temp));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        if (Util.SDK_INT > 23 || mExoPlayer == null) {
            initializePlayer(Uri.parse(mVideoUrl));
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if (Util.SDK_INT > 23) {
            if (mVideoUrl != null) {
                releasePlayer();
            }
        }
    }

    private void initializePlayer(Uri mediaUri) {

        if (mVideoUrl != null) {
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
                mExoPlayer.setPlayWhenReady(true);

            }
        } else {
            mNoVideoImage.setVisibility(View.VISIBLE);
            Picasso.with(getContext())
                    .load(mDirections.getThumbnailURL())
                    .placeholder(R.drawable.temp)
                    .error(R.drawable.temp)
                    .into(mNoVideoImage);
        }
    }

    /**
     * Release ExoPlayer
     */
    private void releasePlayer() {

        //mNotificationManager.cancelAll();
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
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

}
