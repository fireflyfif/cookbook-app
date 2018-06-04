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

package com.example.android.cookbook.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerTitleStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.android.cookbook.R;
import com.example.android.cookbook.model.Step;
import com.example.android.cookbook.ui.fragments.DirectionDetailFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DirectionDetailActivity extends AppCompatActivity {

    private static final String LOG_TAG = DirectionDetailActivity.class.getSimpleName();

    private static final String DIRECTION_LIST_PARCEL_KEY = "direction_key";
    private static final String DIRECTION_CURRENT_KEY = "current_direction_key";

    @BindView(R.id.directions_viewpager)
    ViewPager mDirectionsViewPager;
    @BindView(R.id.pager_title_strip)
    PagerTitleStrip mPagerTitleStrip;
    @BindView(R.id.previous_step)
    ImageButton previousStep;
    @BindView(R.id.next_step)
    ImageButton nextStep;

    private ArrayList<Step> mDirectionsList;
    private DirectionsPagerAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_detail);

        ButterKnife.bind(this);

        // Get the extra from the Intent
        if (getIntent().getExtras() != null) {

            Bundle bundle = getIntent().getExtras();

            mDirectionsList = bundle.getParcelableArrayList(DIRECTION_LIST_PARCEL_KEY);
            Step direction = bundle.getParcelable(DIRECTION_CURRENT_KEY);
            // Get the position by getting the Id of the current Step
            final int currentPosition = direction.getId();

            mAdapter = new DirectionsPagerAdapter(
                    getSupportFragmentManager(), mDirectionsList, currentPosition);

            mDirectionsViewPager.setAdapter(mAdapter);
            // Set the current Item to the position of the ViewPager
            mDirectionsViewPager.setCurrentItem(currentPosition);

            mDirectionsViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                    Log.d(LOG_TAG, "onPageScrolled called");
                }

                @Override
                public void onPageSelected(int position) {

                    Log.d(LOG_TAG, "onPageSelected called");
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                    Log.d(LOG_TAG, "onPageScrollStateChanged called");

                }
            });


            if (currentPosition == 0) {
                previousStep.setVisibility(View.INVISIBLE);
            }

            if (currentPosition == mDirectionsList.size() - 1) {
                nextStep.setVisibility(View.INVISIBLE);
            }

            previousStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentStep = mDirectionsViewPager.getCurrentItem();
                    if (currentStep > 0) {
                        currentStep--;
                        mDirectionsViewPager.setCurrentItem(currentStep);
                        previousStep.setVisibility(View.VISIBLE);

                    }

                    nextStep.setVisibility(View.VISIBLE);

                    if (currentStep == 0){
                        mDirectionsViewPager.setCurrentItem(currentStep);
                        previousStep.setVisibility(View.INVISIBLE);
                    }
                }
            });

            nextStep.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int currentStep = mDirectionsViewPager.getCurrentItem();
                    if (currentStep < mDirectionsList.size() - 1) {
                        currentStep++;
                    }
                    nextStep.setVisibility(View.VISIBLE);
                    previousStep.setVisibility(View.VISIBLE);
                    mDirectionsViewPager.setCurrentItem(currentStep);

                    if (currentStep == mDirectionsList.size() - 1) {
                        nextStep.setVisibility(View.INVISIBLE);
                    }
                }
            });
        }
    }


    /**
     * FragmentStatePagerAdapter that inflates the Fragment with different Directions of the Recipes
     */
    public static class DirectionsPagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Step> mDirectionsList;
        private int mCurrentDirection;


        // Default constructor
        public DirectionsPagerAdapter(FragmentManager fm, ArrayList<Step> directionList, int position) {
            super(fm);
            mDirectionsList = directionList;
            mCurrentDirection = position;
        }

        @Override
        public Fragment getItem(int position) {

            return DirectionDetailFragment.newInstance(mDirectionsList, position);
        }

        @Override
        public int getCount() {

            return mDirectionsList.size();
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            // Set the title of the PagerTitleStrip on each Step
            return "Step " + (position);
        }


        // Method to track down when the item is being destroyed
        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            super.destroyItem(container, position, object);
            //container.removeView((LinearLayout) object);
            Log.i(LOG_TAG, "destroyItem() [position: " + position + "]" + " childCount:" + container.getChildCount());
        }
    }
}
