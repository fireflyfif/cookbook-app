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

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.baking_app.R;
import com.example.android.baking_app.model.Ingredient;
import com.example.android.baking_app.model.RecipesResponse;
import com.example.android.baking_app.remote.MainApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IngredientsFragment extends Fragment {

    private static final String RECIPE_PARCEL_KEY = "recipe_key";
    private static final String INGREDIENT_PARCEL_KEY = "ingredient_key";

    private static RecipesResponse sRecipes;
    private static Ingredient sIngredient;
    private List<RecipesResponse> mRecipesList;
    private List<Ingredient> mIngredientsList;
    // private ArrayList<Ingredient> mIngredientsArrayList;
    private IngredientsAdapter mIngredientsAdapter;

    @BindView(R.id.ingredients_rv)
    RecyclerView mIngredientsRv;
    @BindView(R.id.ingredients_card_view)
    CardView mIngredientCardView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);

        ButterKnife.bind(this, rootView);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            sRecipes = bundle.getParcelable(RECIPE_PARCEL_KEY);
            mIngredientsList = bundle.getParcelableArrayList(INGREDIENT_PARCEL_KEY);

            // mRecipesList = new ArrayList<>();
            mIngredientsList = sRecipes.getIngredients();

            loadIngredients();
        }

        return rootView;
    }

    private void loadIngredients() {

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mIngredientsRv.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(
                mIngredientsRv.getContext(),
                layoutManager.getOrientation());

        if (mIngredientsAdapter == null) {
            mIngredientsAdapter = new IngredientsAdapter(getContext(), mIngredientsList);
            mIngredientsRv.setAdapter(mIngredientsAdapter);
            mIngredientsRv.setHasFixedSize(true);
            mIngredientsRv.addItemDecoration(dividerItemDecoration);

        } else {
            mIngredientsAdapter.setIngredientsData(mIngredientsList);
            mIngredientsAdapter.notifyDataSetChanged();
        }
    }
}
