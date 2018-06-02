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

package com.example.android.cookbook.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.android.cookbook.R;
import com.example.android.cookbook.model.Ingredient;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.example.android.cookbook.utilities.CookbookWidgetProvider.INGREDIENTS_PREFS;
import static com.example.android.cookbook.utilities.CookbookWidgetProvider.PREFERENCE_NAME;

/**
 * Class that is an interface for an adapter between a remote collection view
 * (ListView, GridView, etc) and the underlying data for that view.
 */
public class ListRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;
    private List<Ingredient> mIngredientsList;

    public ListRemoteViewsFactory(Context appContext) {
        mContext = appContext;
    }

    @Override
    public void onCreate() {

        Log.d("RemoteAdapter", "onCreate triggered now");

    }

    @Override
    public void onDataSetChanged() {

        Log.d("RemoteAdapter", "onDataSetChanged triggered now");

        List<Ingredient> ingredientList;

        SharedPreferences sharedPreferences = mContext.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);


        if (sharedPreferences.contains(INGREDIENTS_PREFS)) {


            String jsonIngredients = sharedPreferences.getString(INGREDIENTS_PREFS, null);
            Gson gson = new Gson();
//            Ingredient[] ingredientItems = gson.fromJson(jsonIngredients,
//                    Ingredient[].class);
//
//            ingredientList = Arrays.asList(ingredientItems);
//            ingredientList = new ArrayList<>(ingredientList);

            Type type = new TypeToken<ArrayList<Ingredient>>(){}.getType();
            ingredientList = gson.fromJson(jsonIngredients, type);

            if (ingredientList == null) {
                return;
            }

            mIngredientsList = ingredientList;

        }
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mIngredientsList != null) {
            return mIngredientsList.size();
        }
        return 0;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews views = new RemoteViews(mContext.getPackageName(),
                R.layout.widget_ingredient_item);

        String ingredientName = mIngredientsList.get(position).getIngredient();
        views.setTextViewText(R.id.ingredient_name_tv, ingredientName);

        String ingredientQuantity = String.valueOf(mIngredientsList.get(position).getQuantity());
        views.setTextViewText(R.id.quantity_tv, ingredientQuantity);

        String ingredientMeasure = mIngredientsList.get(position).getMeasure();
        views.setTextViewText(R.id.measure_tv, ingredientMeasure);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
