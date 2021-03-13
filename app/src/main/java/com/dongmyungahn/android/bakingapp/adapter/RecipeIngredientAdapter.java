package com.dongmyungahn.android.bakingapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dongmyungahn.android.bakingapp.R;
import com.dongmyungahn.android.bakingapp.model.RecipeIngredient;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeIngredientAdapter extends RecyclerView.Adapter<RecipeIngredientAdapter.RecipeIngredientAdapterViewHolder> {

    private List<RecipeIngredient> mIngredients;

    @NonNull
    @Override
    public RecipeIngredientAdapter.RecipeIngredientAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ingredient_list_item, viewGroup, false);
        return new RecipeIngredientAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeIngredientAdapter.RecipeIngredientAdapterViewHolder holder, int pos) {

        String ingredientString = "\u2022 ";
        RecipeIngredient ingredient = mIngredients.get(pos);
        if(ingredient != null) {
            ingredientString += ingredient.getIngredient() + " (" + ingredient.getQuantity() + ingredient.getMeasure() + ")";
            holder.mTvRecipeIngredient.setText(ingredientString);
        }
    }

    @Override
    public int getItemCount() {
        if(mIngredients == null) return 0;
        return mIngredients.size();
    }


    public void setIngredientData(List<RecipeIngredient> ingredients) {
        mIngredients = ingredients;
        notifyDataSetChanged();
    }

    public class RecipeIngredientAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_recipe_ingredient)TextView mTvRecipeIngredient;

        public RecipeIngredientAdapterViewHolder(final View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
