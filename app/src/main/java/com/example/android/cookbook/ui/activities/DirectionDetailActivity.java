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

import com.example.android.cookbook.R;
import com.example.android.cookbook.model.Step;
import com.example.android.cookbook.ui.fragments.DirectionDetailFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DirectionDetailActivity extends AppCompatActivity {

    private static final String CURRENT_POSITION_KEY = "current_position";
    private static final String DIRECTION_LIST_PARCEL_KEY = "direction_key";
    private static final String DIRECTION_CURRENT_KEY = "current_direction_key";

    @BindView(R.id.directions_viewpager)
    ViewPager mDirectionsViewPager;
    @BindView(R.id.pager_title_strip)
    PagerTitleStrip mPagerTitleStrip;

    private ArrayList<Step> mDirectionsList;
    private Step mDirection;
    private int mPosition;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_direction_detail);

        ButterKnife.bind(this);

        // TODO: Set the Toolbar first
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent() != null) {
            Intent intent = getIntent();
            mDirectionsList = intent.getParcelableArrayListExtra(DIRECTION_LIST_PARCEL_KEY);
            mDirection = intent.getParcelableExtra(DIRECTION_CURRENT_KEY);

            if (mDirection != null) {
                mPosition = mDirection.getId();

                // TODO: Issue!!! The ViewPager doesn't update when is sliding
                DirectionsPagerAdapter adapter = new DirectionsPagerAdapter(
                        getSupportFragmentManager(), mDirectionsList, mPosition);

                mDirectionsViewPager.setAdapter(adapter);

            }
        }
    }


    /**
     * FragmentStatePagerAdapter that inflates the Fragment with different Directions of the Recipes
     */
    static class DirectionsPagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Step> mDirectionsList;
        //private Step mDirection;
        private int mCurrentDirection;

        // Default constructor
        public DirectionsPagerAdapter(FragmentManager fm, ArrayList<Step> directionList, int position) {
            super(fm);
            mDirectionsList = directionList;
            mCurrentDirection = position;
        }

        @Override
        public Fragment getItem(int position) {

//            Bundle bundle = new Bundle();
//            bundle.putInt(CURRENT_POSITION_KEY, position);
//            bundle.putParcelableArrayList(DIRECTION_LIST_PARCEL_KEY, mDirectionsList);

            DirectionDetailFragment videoFragment = DirectionDetailFragment
                    .newInstance(mDirectionsList, position);

            //videoFragment.setArguments(bundle);

            return videoFragment;
        }

        // Supposed to update the view with new Steps/Directions but it doesn't work
        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public int getCount() {

            return mDirectionsList.size();
        }
    }
}
