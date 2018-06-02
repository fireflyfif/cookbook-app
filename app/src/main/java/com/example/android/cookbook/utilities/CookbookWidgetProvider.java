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

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.cookbook.R;

/**
 * Implementation of App Widget functionality.
 */
public class CookbookWidgetProvider extends AppWidgetProvider {

    private static final String LOG_TAG = CookbookWidgetProvider.class.getSimpleName();

    // Constants for Shared Preference
    public static final String PREFERENCE_NAME = "ingredients_prefs";
    public static final String INGREDIENTS_PREFS = "ingredients_favorite";
    public static final String RECIPE_NAME_PREFS = "recipe_name_prefs";


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        SharedPreferences sharedPreferences = context.getSharedPreferences(
                PREFERENCE_NAME, Context.MODE_PRIVATE);

        if (sharedPreferences.contains(INGREDIENTS_PREFS)) {

            String recipeName = sharedPreferences.getString(RECIPE_NAME_PREFS, "Recipe Name");
            Log.d(LOG_TAG, "Title of the Recipe: " + recipeName);

            // Construct the RemoteViews object
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredients_list);
            views.setTextViewText(R.id.widget_recipe_name, recipeName);

            AppWidgetManager.getInstance(context);

            // Setup the intent which points to the IngredientsWidgetService which will
            // provide the views for this collection
            Intent intent = new Intent(context, IngredientsWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);

            // When intents are compared, the extras are ignored, so we need to embed the extras
            // into the data so that the extras will not be ignored.
            // source: https://android.googlesource.com/platform/development/+/master/samples/StackWidget/src/com/example/android/stackwidget/StackWidgetProvider.java
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            // Set the remote adapter for the ListView with ingredients
            views.setRemoteAdapter(R.id.appwidget_ingredient_list, intent);

            // TODO: Setup the Pending Intent to launch the Activity with the Recipe when clicked

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.widget_recipe_name);
            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetId, R.id.appwidget_ingredient_list);
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

