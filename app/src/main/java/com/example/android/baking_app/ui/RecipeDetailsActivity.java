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
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.baking_app.R;
import com.example.android.baking_app.model.Ingredient;
import com.example.android.baking_app.model.RecipesResponse;
import com.example.android.baking_app.model.Step;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Detail Activity for a selected Recipe
 *
 * The XML Layout with the ViewPager is made with the help of this Tutorial by Codelabs:
 * https://codelabs.developers.google.com/codelabs/material-design-style/index.html?index=..%2F..%2Findex#0
 */
public class RecipeDetailsActivity extends AppCompatActivity {

    private static final String RECIPE_PARCEL_KEY = "recipe_key";
    private static final String INGREDIENT_PARCEL_KEY = "ingredient_key";

    private static RecipesResponse sRecipes;
    private List<Ingredient> mIngredientsList;

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.tabs)
    TabLayout tabs;
    @BindView(R.id.recipe_name)
    TextView recipeTitle;
    @BindView(R.id.servings_tv)
    TextView servingsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        ButterKnife.bind(this);

        if (getIntent().getExtras().get(RECIPE_PARCEL_KEY) != null) {

            Bundle bundle = getIntent().getExtras();
            sRecipes = bundle.getParcelable(RECIPE_PARCEL_KEY);

            if (bundle.get(INGREDIENT_PARCEL_KEY) != null) {
                mIngredientsList = bundle.getParcelableArrayList(INGREDIENT_PARCEL_KEY);
                mIngredientsList = sRecipes.getIngredients();
            }

            recipeTitle.setText(sRecipes.getName());
            servingsTextView.setText(String.valueOf(sRecipes.getServings()));
        }

        setSupportActionBar(toolbar);
        setupViewPager(viewPager);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tabs.setupWithViewPager(viewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), sRecipes);
        adapter.addFragment(new IngredientsFragment(), "Ingredients");
        adapter.addFragment(new DirectionsFragment(), "Directions");
        viewPager.setAdapter(adapter);
    }

    /**
     * Class Adapter for the Fragments
     */
    static class PagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentLst = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private RecipesResponse mRecipes;
        // Not sure about those variables
        private ArrayList<Ingredient> mIngredientsList;
        private ArrayList<Step> mStepsList;

        public PagerAdapter(FragmentManager fm, RecipesResponse recipes) {
            super(fm);
            mRecipes = recipes;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return IngredientsFragment.newInstance(mRecipes, mIngredientsList);
                case 1:
                    return DirectionsFragment.newInstance(mRecipes, mStepsList);
                default:
                    return IngredientsFragment.newInstance(mRecipes, mIngredientsList);
            }

            // return mFragmentLst.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentLst.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentLst.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                return false;
            default:
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
