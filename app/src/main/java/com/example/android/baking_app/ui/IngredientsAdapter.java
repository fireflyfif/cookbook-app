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
import android.widget.TextView;

import com.example.android.baking_app.R;
import com.example.android.baking_app.model.Ingredient;
import com.example.android.baking_app.model.RecipesResponse;

import java.util.ArrayList;
import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientViewHolder> {

    private List<Ingredient> mIngredientsList;
    private Context mContext;

    public IngredientsAdapter(Context context, List<Ingredient> ingredientsList) {
        mContext = context;
        mIngredientsList = ingredientsList;
    }

    @NonNull
    @Override
    public IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.ingredient_item, parent, false);

        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder holder, int position) {
        
        Ingredient currentIngredient = mIngredientsList.get(position);

        String ingredientNameString = currentIngredient.getIngredient();
        holder.ingredientName.setText(ingredientNameString);

        String ingredientQuantityString = String.valueOf(currentIngredient.getQuantity());
        holder.ingredientQuantity.setText(ingredientQuantityString);

        String ingredientMeasureString = currentIngredient.getMeasure();
        holder.ingredientMeasure.setText(ingredientMeasureString);

    }

    @Override
    public int getItemCount() {
        if (mIngredientsList == null) {
            return 0;
        } else {
            return mIngredientsList.size();
        }
    }

    public class IngredientViewHolder extends RecyclerView.ViewHolder {

        TextView ingredientName;
        TextView ingredientQuantity;
        TextView ingredientMeasure;

        public IngredientViewHolder(View itemView) {
            super(itemView);

            ingredientName = itemView.findViewById(R.id.ingredient_name_tv);
            ingredientQuantity = itemView.findViewById(R.id.quantity_tv);
            ingredientMeasure = itemView.findViewById(R.id.measure_tv);
        }
    }

    public void setIngredientsData(List<Ingredient> ingredientList) {
        mIngredientsList = ingredientList;
        notifyDataSetChanged();
    }
}
