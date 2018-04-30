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

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.baking_app.R;
import com.example.android.baking_app.model.RecipesResponse;

import java.util.List;

public class MasterRecipesAdapter extends RecyclerView.Adapter<MasterRecipesAdapter.RecipesViewHolder> {

    private List<RecipesResponse> mRecipeList;
    private Context mContext;

    public MasterRecipesAdapter(Context context, List<RecipesResponse> recipeList) {
        mContext = context;
        mRecipeList = recipeList;
    }

    @NonNull
    @Override
    public RecipesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.recipe_card_item, parent, false);

        return new RecipesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipesViewHolder holder, int position) {

        RecipesResponse currentRecipe = mRecipeList.get(position);
        holder.recipeName.setText(currentRecipe.getName());
        // TODO: Prepare the image to display recipe picture if any

    }

    @Override
    public int getItemCount() {
        if (mRecipeList != null) {
            return mRecipeList.size();
        } else {
            return 0;
        }
    }

    public class RecipesViewHolder extends RecyclerView.ViewHolder {

        TextView recipeName;
        ImageView recipeImage;

        public RecipesViewHolder(View itemView) {
            super(itemView);

            recipeName = itemView.findViewById(R.id.recipe_name_tv);
            recipeImage = itemView.findViewById(R.id.recipe_picture_iv);
        }
    }

    public void setRecipeData(List<RecipesResponse> recipes) {
        mRecipeList = recipes;
        notifyDataSetChanged();
    }
}
