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

package com.example.android.cookbook.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.cookbook.R;
import com.example.android.cookbook.model.RecipesResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class MasterRecipesAdapter extends RecyclerView.Adapter<MasterRecipesAdapter.RecipesViewHolder> {

    private List<RecipesResponse> mRecipeList;
    private Context mContext;

    private OnRecipeClickListener mClickHandler;

        public interface OnRecipeClickListener {
        void onRecipeClick(RecipesResponse recipe);
    }

    /**
     * Constructor of the adapter
     *
     * @param context      the current context
     * @param recipeList   displays the list of recipes
     * @param clickHandler is the on recipe click handler
     */
    public MasterRecipesAdapter(Context context, List<RecipesResponse> recipeList,
                                OnRecipeClickListener clickHandler) {
        mContext = context;
        mRecipeList = recipeList;
        mClickHandler = clickHandler;
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

        String recipeServing = String.valueOf(currentRecipe.getServings());
        holder.recipeServings.setText(recipeServing);

        // Prepare the image to display recipe picture if any
        if (!currentRecipe.getImage().equals("")) {
            String recipeImage = currentRecipe.getImage();

            Picasso.with(mContext)
                    .load(recipeImage)
                    .placeholder(R.drawable.cookbook_bg_1)
                    .error(R.drawable.cookbook_bg_1)
                    .into(holder.recipeImage);
        } else {
            Picasso.with(mContext)
                    .load(R.drawable.cookbook_bg_1)
                    .placeholder(R.drawable.cookbook_bg_1)
                    .error(R.drawable.cookbook_bg_1)
                    .into(holder.recipeImage);
        }

    }

    @Override
    public int getItemCount() {
        if (mRecipeList != null) {
            return mRecipeList.size();
        } else {
            return 0;
        }
    }

    public class RecipesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView recipeName;
        TextView recipeServings;
        ImageView recipeImage;

        public RecipesViewHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            recipeName = itemView.findViewById(R.id.recipe_name_tv);
            recipeServings = itemView.findViewById(R.id.recipe_serving_tv);
            recipeImage = itemView.findViewById(R.id.recipe_picture_iv);
        }

        @Override
        public void onClick(View view) {
            RecipesResponse currentRecipe = mRecipeList.get(getAdapterPosition());
            mClickHandler.onRecipeClick(currentRecipe);
        }
    }

    public void setRecipeData(List<RecipesResponse> recipes) {
        mRecipeList = recipes;
        notifyDataSetChanged();
    }
}
