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

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.cookbook.IdlingResource.SimpleIdlingResource;
import com.example.android.cookbook.R;
import com.example.android.cookbook.model.Ingredient;
import com.example.android.cookbook.model.RecipesResponse;
import com.example.android.cookbook.model.Step;
import com.example.android.cookbook.ui.fragments.DirectionDetailFragment;
import com.example.android.cookbook.ui.fragments.DirectionsListFragment;
import com.example.android.cookbook.ui.fragments.IngredientsFragment;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.cookbook.utilities.Utils.INGREDIENTS_PREFS;
import static com.example.android.cookbook.utilities.Utils.PREFERENCE_NAME;
import static com.example.android.cookbook.utilities.Utils.PREFERENCE_RECIPE_ID;
import static com.example.android.cookbook.utilities.Utils.RECIPE_NAME_PREFS;

/**
 * Detail Activity for a selected Recipe
 * <p>
 * The XML Layout with the ViewPager is made with the help of this Tutorial by Codelabs:
 * https://codelabs.developers.google.com/codelabs/material-design-style/index.html?index=..%2F..%2Findex#0
 */
public class RecipeDetailsActivity extends AppCompatActivity implements DirectionsListFragment.StepOnClickHandler {

    private static final String LOG_TAG = RecipeDetailsActivity.class.getSimpleName();

    private static final String RECIPE_PARCEL_KEY = "recipe_key";

    private static RecipesResponse sRecipes;
    private ArrayList<Ingredient> mIngredientsList;
    private ArrayList<Step> mDirectionsList;

    private boolean mTwoPane;

    @Nullable
    private static SimpleIdlingResource mIdlingResource;

    @Nullable
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @Nullable
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @Nullable
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @Nullable
    @BindView(R.id.tabs)
    TabLayout tabs;
    @Nullable
    @BindView(R.id.servings_tv)
    TextView servingsTextView;
    @BindView(R.id.recipe_image_iv)
    ImageView recipeImage;
    @BindView(R.id.main_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.add_recipe_to_widget_button)
    FloatingActionButton addRecipeToWidget;


    /**
     * This method will only be called from test.
     * It will
     * instantiate a new instance of SimpleIdlingResource if the IdlingResource is null.
     *
     * @return the IdlingResource
     */
    @VisibleForTesting
    @NonNull
    public static IdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_details);

        ButterKnife.bind(this);

        // First set the Action Bar
        setSupportActionBar(toolbar);

        // Check if there is an Action Bar and then set the Home Up Button
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        if (getIntent().getExtras() != null) {

            Bundle bundle = getIntent().getExtras();
            sRecipes = bundle.getParcelable(RECIPE_PARCEL_KEY);
            //mIngredientsList = bundle.getParcelableArrayList(INGREDIENT_PARCEL_KEY);
            if (sRecipes != null) {
                mIngredientsList = (ArrayList<Ingredient>) sRecipes.getIngredients();
                mDirectionsList = (ArrayList<Step>) sRecipes.getSteps();

                // Populate the UI with details of the current recipe
                populateUi(sRecipes);

                addRecipeToWidget.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        saveIngredients(RecipeDetailsActivity.this, mIngredientsList, sRecipes);
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Ingredients added to the widget.",
                                Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                });
            }


            if (findViewById(R.id.two_pane_layout) != null) {
                mTwoPane = true;

                if (savedInstanceState == null) {

                    FragmentManager fragmentManager = getSupportFragmentManager();

                    // Create new Ingredients Fragment
                    IngredientsFragment ingredientsFragment = IngredientsFragment.newInstance(sRecipes, mIngredientsList);
                    fragmentManager.beginTransaction()
                            .add(R.id.ingredients_container, ingredientsFragment)
                            .commit();

                    // Create new Direction/Step Fragment
                    DirectionsListFragment stepFragment = DirectionsListFragment.newInstance(sRecipes, mDirectionsList);
                    fragmentManager.beginTransaction()
                            .add(R.id.directions_container, stepFragment)
                            .commit();

                    // Create new Direction Detail Fragment that will display the videos for each Recipe Direction
                    Step step = mDirectionsList.get(0);
                    int clickedStep = step.getId();
                    DirectionDetailFragment videoFragment = DirectionDetailFragment.newInstance(mDirectionsList, clickedStep);
                    fragmentManager.beginTransaction()
                            .add(R.id.direction_video_container, videoFragment)
                            .commit();
                }

            } else {
                mTwoPane = false;

                // Set up the ViewPager for a phone layout

                if (viewPager != null) {
                    setupViewPager(viewPager);
                }

                if (tabs != null) {
                    tabs.setupWithViewPager(viewPager);
                }

                // Set the title of the Recipe on the Collapsing Toolbar
                if ((collapsingToolbar != null) && (sRecipes != null)) {
                    collapsingToolbar.setTitle(sRecipes.getName());
                }
            }
        }
    }

    private void populateUi(RecipesResponse recipes) {

        //Set the servings of the current recipe
        assert servingsTextView != null;
        servingsTextView.setText(String.valueOf(recipes.getServings()));

        Log.d(LOG_TAG, "Name of the Recipe: " + recipes.getName());

        // Set the title of the Recipe on the Collapsing Toolbar
        String recipeName = null;
        if (collapsingToolbar != null) {
            recipeName = recipes.getName();
            collapsingToolbar.setTitle(recipeName);
        }

        // Set font to the expandable title
        // used resource: https://stackoverflow.com/a/33174619/8132331
        Typeface font = ResourcesCompat.getFont(this, R.font.dosis_medium);
        collapsingToolbar.setCollapsedTitleTypeface(font);
        collapsingToolbar.setExpandedTitleTypeface(font);
        collapsingToolbar.setExpandedTitleColor(getResources().getColor(R.color.colorGray));

        // Set the image of the current recipe
        String recipeImageString = recipes.getImage();
        if (recipeImageString.isEmpty()) {
            recipeImageString = String.valueOf(getResources().getDrawable(R.drawable.cookbook_bg_1));
        }

        Log.d(LOG_TAG, "Path to the current Recipe: " + recipeImageString);

        Picasso.get()
                .load(setTempImage(recipeName))
                .placeholder(R.drawable.cookbook_bg_1)
                .error(R.drawable.cookbook_bg_1)
                .into(recipeImage);
    }

    private int setTempImage(String recipeName) {
        int imageId;

        if (recipeName.contains("Pie")) {
            imageId = R.drawable.nutella_pie;
        } else if (recipeName.contains("Brownies")) {
            imageId = R.drawable.brownies;
        } else if (recipeName.contains("Yellow")) {
            imageId = R.drawable.yellow_cake;
        } else {
            imageId = R.drawable.cheesecake;
        }

        return imageId;
    }

    /**
     * Method for setting up the ViewPager for a phone layout
     *
     * @param viewPager that will be displayed
     */
    private void setupViewPager(ViewPager viewPager) {
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), sRecipes);
        adapter.addFragment(new IngredientsFragment(), "Ingredients");
        adapter.addFragment(new DirectionsListFragment(), "Directions");
        viewPager.setAdapter(adapter);
    }


    @Override
    public void onStepClick(Step step, ArrayList<Step> stepList) {
        Toast.makeText(this, "Step " + step.getId() , Toast.LENGTH_SHORT).show();

        if (mTwoPane) {
            DirectionDetailFragment videoFragment = DirectionDetailFragment.newInstance(stepList, step.getId());
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.direction_video_container, videoFragment)
                    .commit();

        } else {
            DirectionsListFragment.clickStepIntent(this, step, stepList);
        }
    }

    /**
     * Method for saving the list of ingredients into SharedPreferences,
     * using Gson for retrieving it from the JSON
     *
     * help resource: http://androidopentutorials.com/android-sharedpreferences-tutorial-and-example/
     *
     * @param context is the current context
     * @param ingredientsList is the list of ingredients
     */
    public void saveIngredients(Context context, List<Ingredient> ingredientsList, RecipesResponse recipe) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String jsonIngredients = gson.toJson(ingredientsList);
        Log.d(LOG_TAG, "Ingredients saved: " + jsonIngredients);

        String recipeName = recipe.getName();
        Log.d(LOG_TAG, "Recipe name saved: " + recipeName);
        int recipeId = recipe.getId();
        Log.d(LOG_TAG, "Recipe id saved: " + recipeId);

        editor.putString(INGREDIENTS_PREFS, jsonIngredients);
        editor.putString(RECIPE_NAME_PREFS, recipeName);
        editor.putInt(PREFERENCE_RECIPE_ID, recipeId);
        editor.apply();
    }

    /**
     * Class Adapter for the Fragments
     */
    static class PagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentLst = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        private RecipesResponse mRecipes;

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
                    return DirectionsListFragment.newInstance(mRecipes, mStepsList);
                default:
                    return IngredientsFragment.newInstance(mRecipes, mIngredientsList);
            }

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
