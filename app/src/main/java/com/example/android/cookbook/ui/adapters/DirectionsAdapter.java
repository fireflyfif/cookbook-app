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
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.cookbook.R;
import com.example.android.cookbook.model.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DirectionsAdapter extends RecyclerView.Adapter<DirectionsAdapter.DirectionsViewHolder> {

    private ArrayList<Step> mDirectionsList;
    private Context mContext;

    private StepOnClickHandler mCallback;

    public interface StepOnClickHandler {
        void onStepClick (Step step, ArrayList<Step> stepList);
    }


    public DirectionsAdapter(Context context, ArrayList<Step> directionsList, StepOnClickHandler callback) {
        mContext = context;
        mDirectionsList = directionsList;
        mCallback = callback;
    }

    @NonNull
    @Override
    public DirectionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext)
                .inflate(R.layout.direction_item, parent, false);

        return new DirectionsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DirectionsViewHolder holder, int position) {
        Step currentStep = mDirectionsList.get(position);

        if (currentStep.getId() < 1) {
            holder.directionLabel.setVisibility(View.GONE);
            holder.directionNumber.setText("");
            TextViewCompat
                    .setTextAppearance(holder.directionShortDescr,
                    android.R.style.TextAppearance_DeviceDefault_Large);
            holder.directionShortDescr.setTypeface(
                    Typeface.create("sans-serif-smallcaps", Typeface.NORMAL));
        } else {
            holder.directionNumber.setText(String.valueOf(currentStep.getId()));
        }

        holder.directionShortDescr.setText(currentStep.getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (mDirectionsList == null) {
            return 0;
        } else {
            return mDirectionsList.size();
        }
    }

    public class DirectionsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.step_title_label)
        TextView directionLabel;
        @BindView(R.id.step_number_tv)
        TextView directionNumber;
        @BindView(R.id.step_short_description_tv)
        TextView directionShortDescr;

        public DirectionsViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Step currentStep = mDirectionsList.get(getAdapterPosition());
            mCallback.onStepClick(currentStep, mDirectionsList);
        }
    }

    public void setDirectionsList (ArrayList<Step> stepList) {
        mDirectionsList = stepList;
        notifyDataSetChanged();
    }
}
