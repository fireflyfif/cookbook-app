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

package com.example.android.baking_app.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.android.baking_app.R;
import com.example.android.baking_app.model.JSONResponse;
import com.example.android.baking_app.model.RecipesResponse;
import com.example.android.baking_app.remote.MainApplication;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MasterRecipesFragment extends Fragment {

    private static final String LOG_TAG = "MasterRecipesFragment";
    private MasterRecipesAdapter mAdapter;
    private List<RecipesResponse> mRecipeList;

    @BindView(R.id.recipes_rv)
    RecyclerView mRecipesRv;

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

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecipesRv.setLayoutManager(layoutManager);
        mRecipeList = new ArrayList<>();

        loadRecipes();

        return rootView;
    }

    private void loadRecipes() {

        MainApplication.sManager.getRecipes(new Callback<List<RecipesResponse>>() {

            @Override
            public void onResponse(Call<List<RecipesResponse>> call, Response<List<RecipesResponse>> response) {
                if (response.isSuccessful()) {

                    mRecipeList = response.body();

                    if (mAdapter == null) {
                        mAdapter = new MasterRecipesAdapter(getActivity(), mRecipeList);
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

                Log.d(LOG_TAG, t.toString());
            }
        });
    }
}
