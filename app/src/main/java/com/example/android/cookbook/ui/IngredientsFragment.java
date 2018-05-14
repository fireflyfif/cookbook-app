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

import com.example.android.cookbook.R;
import com.example.android.cookbook.model.Ingredient;
import com.example.android.cookbook.model.RecipesResponse;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

    /**
     * New Instance constructor for creating Fragment with arguments
     *
     * Followed example from Codepath: https://guides.codepath.com/android/viewpager-with-fragmentpageradapter
     *
     * @param recipes
     * @return
     */
    public static IngredientsFragment newInstance(RecipesResponse recipes, ArrayList<Ingredient> ingredientsList) {
        IngredientsFragment ingredientsFragment = new IngredientsFragment();
        Bundle arguments = new Bundle();
        arguments.putParcelable(RECIPE_PARCEL_KEY, recipes);
        arguments.putParcelableArrayList(INGREDIENT_PARCEL_KEY, ingredientsList);
        ingredientsFragment.setArguments(arguments);

        return ingredientsFragment;
    }

    public IngredientsFragment() {}

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            sRecipes = getArguments().getParcelable(RECIPE_PARCEL_KEY);
            mIngredientsList = getArguments().getParcelableArrayList(INGREDIENT_PARCEL_KEY);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ingredients, container, false);

        ButterKnife.bind(this, rootView);

        Bundle bundle = getActivity().getIntent().getExtras();
        if (bundle != null) {
            sRecipes = bundle.getParcelable(RECIPE_PARCEL_KEY);
            mIngredientsList = bundle.getParcelableArrayList(INGREDIENT_PARCEL_KEY);

            loadIngredients(sRecipes);
        }

        return rootView;
    }

    private void loadIngredients(RecipesResponse recipes) {

        getActivity().setTitle(recipes.getName());
        // mRecipesList = new ArrayList<>();
        mIngredientsList = recipes.getIngredients();

        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mIngredientsRv.setLayoutManager(layoutManager);

        // Add divider between each item in the RecyclerView,
        // help from this SO post: https://stackoverflow.com/a/40217754/8132331
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
