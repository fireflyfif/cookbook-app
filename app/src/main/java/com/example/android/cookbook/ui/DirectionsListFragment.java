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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.android.cookbook.R;
import com.example.android.cookbook.model.RecipesResponse;
import com.example.android.cookbook.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/*
Dynamic Fragment that displays the list of Steps
 */
public class DirectionsListFragment extends Fragment implements DirectionsAdapter.StepOnClickHandler {

    private static final String RECIPE_PARCEL_KEY = "recipe_key";
    private static final String DIRECTION_LIST_PARCEL_KEY = "direction_key";
    private static final String DIRECTION_CURRENT_KEY = "current_direction_key";

    private static RecipesResponse sRecipes;
    private static Step sDirections;
    private ArrayList<Step> mDirectionsList;
    private DirectionsAdapter mDirectionsAdapter;
    private boolean mTwoPane;

    @BindView(R.id.directions_rv)
    RecyclerView mDirectionsRv;
    @BindView(R.id.directions_card_view)
    CardView mDirectionsCard;


    // Mandatory empty constructor
    public DirectionsListFragment() {}

    public static DirectionsListFragment newInstance(RecipesResponse recipes, ArrayList<Step> stepsList) {
        DirectionsListFragment directionsListFragment = new DirectionsListFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(RECIPE_PARCEL_KEY, recipes);
        arguments.putParcelableArrayList(DIRECTION_LIST_PARCEL_KEY, stepsList);
        directionsListFragment.setArguments(arguments);

        return directionsListFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            sRecipes = getArguments().getParcelable(RECIPE_PARCEL_KEY);
            mDirectionsList = getArguments().getParcelableArrayList(DIRECTION_LIST_PARCEL_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_directions_list, container, false);

        ButterKnife.bind(this, rootView);

        if (getActivity().getIntent().getExtras() != null) {
            Bundle bundle = getActivity().getIntent().getExtras();

            sRecipes = bundle.getParcelable(RECIPE_PARCEL_KEY);
            mDirectionsList = bundle.getParcelableArrayList(DIRECTION_LIST_PARCEL_KEY);

            mDirectionsList = (ArrayList<Step>) sRecipes.getSteps();

            loadDirections();
        }

        return rootView;
    }

    private void loadDirections() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mDirectionsRv.setLayoutManager(layoutManager);

        // Add divider between each item in the RecyclerView,
        // help from this SO post: https://stackoverflow.com/a/40217754/8132331
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mDirectionsRv.getContext(),
                layoutManager.getOrientation());

        if (mDirectionsAdapter == null) {
            mDirectionsAdapter = new DirectionsAdapter(getContext(), mDirectionsList, this);
            mDirectionsRv.setHasFixedSize(true);
            mDirectionsRv.setAdapter(mDirectionsAdapter);
            mDirectionsRv.addItemDecoration(dividerItemDecoration);
        } else {
            mDirectionsAdapter.setDirectionsList(mDirectionsList);
            mDirectionsAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onStepClick(Step step, ArrayList<Step> stepList) {
        Toast.makeText(getContext(), "Clicked Step " + step, Toast.LENGTH_SHORT).show();

        if (mTwoPane) {

            // Handle two pane case
            DirectionDetailFragment detailFragment = new DirectionDetailFragment();

        }

        // Start new Activity via Intent with arguments
        Bundle arguments = new Bundle();
        //arguments.putParcelable(RECIPE_PARCEL_KEY, sRecipes);
        arguments.putParcelableArrayList(DIRECTION_LIST_PARCEL_KEY, stepList);
        arguments.putParcelable(DIRECTION_CURRENT_KEY, step);

        Intent intent = new Intent(getActivity(), DirectionDetailActivity.class);
        intent.putExtras(arguments);
        if (getActivity() != null) {
            getActivity().startActivity(intent);
        }

    }
}
