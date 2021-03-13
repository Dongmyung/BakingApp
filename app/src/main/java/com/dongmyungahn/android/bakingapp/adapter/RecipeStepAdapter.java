package com.dongmyungahn.android.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dongmyungahn.android.bakingapp.R;
import com.dongmyungahn.android.bakingapp.model.RecipeStep;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepAdapter extends RecyclerView.Adapter<RecipeStepAdapter.RecipeStepAdapterViewHolder> {

    private List<RecipeStep> mSteps;

    private final RecipeStepAdapterOnClickHandler mClickHandler;

    public interface RecipeStepAdapterOnClickHandler {
        void onClick(int stepPos);
    }

    public RecipeStepAdapter(RecipeStepAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public RecipeStepAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.step_list_item, viewGroup, false);
        return new RecipeStepAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeStepAdapterViewHolder holder, int pos) {
        RecipeStep step = mSteps.get(pos);
        String stepString = null;
        if(step != null) {
            stepString = step.getId() + ". " + step.getShortDescription();
            holder.mTvRecipeStep.setText(stepString);
        }
    }

    @Override
    public int getItemCount() {
        if(mSteps == null) return 0;
        return mSteps.size();
    }

    public void setStepData(List<RecipeStep> steps) {
        mSteps = steps;
        notifyDataSetChanged();
    }

    public class RecipeStepAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.tv_recipe_step)
        TextView mTvRecipeStep;

        public RecipeStepAdapterViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int pos = getAdapterPosition();
            mClickHandler.onClick(pos);
        }
    }
}
