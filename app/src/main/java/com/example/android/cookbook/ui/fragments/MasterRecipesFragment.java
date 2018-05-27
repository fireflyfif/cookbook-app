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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.android.cookbook.R;
import com.example.android.cookbook.model.Ingredient;
import com.example.android.cookbook.model.RecipesResponse;
import com.example.android.cookbook.remote.MainApplication;
import com.example.android.cookbook.ui.adapters.MasterRecipesAdapter;
import com.example.android.cookbook.ui.activities.RecipeDetailsActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterRecipesFragment extends Fragment implements MasterRecipesAdapter.OnRecipeClickListener {

    private static final String LOG_TAG = "MasterRecipesFragment";
    private static final String RECIPE_PARCEL_KEY = "recipe_key";
    private static final String INGREDIENT_PARCEL_KEY = "ingredient_key";

    private MasterRecipesAdapter mAdapter;
    private List<RecipesResponse> mRecipeList;
    private ArrayList<Ingredient> mIngredientsList;
    private RecipesResponse mRecipe;
    private Ingredient mIngredient;

    @BindView(R.id.recipes_rv)
    RecyclerView mRecipesRv;
    @BindView(R.id.error_message_tv)
    TextView mErrorMessage;
    @BindView(R.id.loading_indicator)
    ProgressBar mLoadingIndicator;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    // Mandatory empty constructor
    public MasterRecipesFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_master_recipes, container, false);

        ButterKnife.bind(this, rootView);

        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        if (isTablet) {
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(
                    getActivity(), 3);
            mRecipesRv.setLayoutManager(gridLayoutManager);
        } else {
            final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
            mRecipesRv.setLayoutManager(layoutManager);
        }

        mRecipeList = new ArrayList<>();
        mIngredientsList = new ArrayList<>();

        // Show the Progress Bar
        mLoadingIndicator.setVisibility(View.VISIBLE);

        loadRecipes();

        return rootView;
    }


    /**
     * Method for loading Recipes from the JSON, using Retrofit asynchronously
     */
    private void loadRecipes() {

        MainApplication.sManager.getRecipes(new Callback<List<RecipesResponse>>() {

            @Override
            public void onResponse(Call<List<RecipesResponse>> call, Response<List<RecipesResponse>> response) {
                if (response.isSuccessful()) {
                    // Hide the Progress Bar
                    mLoadingIndicator.setVisibility(View.INVISIBLE);

                    mRecipeList = response.body();

                    if (mAdapter == null) {
                        mAdapter = new MasterRecipesAdapter(getContext(), mRecipeList,
                                MasterRecipesFragment.this);
                        mRecipesRv.setHasFixedSize(true);
                        mRecipesRv.setAdapter(mAdapter);

                    } else {
                        mAdapter.setRecipeData(mRecipeList);
                        mAdapter.notifyDataSetChanged();
                    }

                    int statusCode = response.code();
                    Log.d(LOG_TAG, "Response code: " + statusCode);

                } else {
                    int statusCode = response.code();
                    Log.d(LOG_TAG, "Response code: " + statusCode);
                }
            }

            @Override
            public void onFailure(Call<List<RecipesResponse>> call, Throwable t) {
                // Hide the Progress Bar
                mLoadingIndicator.setVisibility(View.INVISIBLE);

                // Show the error message for no Connectivity
                showErrorMessage();
                Log.d(LOG_TAG, t.toString());
            }
        });
    }

    /**
     * Method that shows error message when there is a problem fetching the data or
     * there is no internet connection
     */
    private void showErrorMessage() {
        // Hide the list with recipes
        mRecipesRv.setVisibility(View.INVISIBLE);
        // Show the error message
        mErrorMessage.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRecipeClick(RecipesResponse recipe) {
        mRecipe = recipe;

        Bundle bundle = new Bundle();
        bundle.putParcelable(RECIPE_PARCEL_KEY, mRecipe);
        bundle.putParcelableArrayList(INGREDIENT_PARCEL_KEY, mIngredientsList);

        Intent intent = new Intent(getContext(), RecipeDetailsActivity.class);
        intent.putExtras(bundle);
        getContext().startActivity(intent);
    }
}
