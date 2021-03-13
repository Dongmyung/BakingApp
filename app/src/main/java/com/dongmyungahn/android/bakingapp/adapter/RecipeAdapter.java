package com.dongmyungahn.android.bakingapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dongmyungahn.android.bakingapp.R;
import com.dongmyungahn.android.bakingapp.model.Recipe;
import com.dongmyungahn.android.bakingapp.ui.RecipeDetailActivity;
import com.dongmyungahn.android.bakingapp.utilities.Constants;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeAdapterViewHolder> {

    private List<Recipe> mRecipes;


    @Override
    public RecipeAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_card_item, viewGroup, false);
        return new RecipeAdapterViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(RecipeAdapterViewHolder holder, int pos) {
        // Bind Data
        String imageUrl = mRecipes.get(pos).getImage();
        if(imageUrl != null && !imageUrl.equals("")) {
            Picasso.with(holder.mIvRecipeImage.getContext())
                    .load(mRecipes.get(pos).getImage())
                    .placeholder(R.drawable.ic_recipe_image)
                    .error(R.drawable.ic_recipe_image)
                    .into(holder.mIvRecipeImage);
        } else {
            holder.mIvRecipeImage.setImageResource(R.drawable.ic_recipe_image);
        }

        holder.mTvRecipeName.setText(mRecipes.get(pos).getName());
        holder.mTvRecipeServings.setText(Integer.toString(mRecipes.get(pos).getServings())+" Servings");
    }

    @Override
    public int getItemCount() {
        if(mRecipes == null) return 0;
        return mRecipes.size();
    }

    public void setRecipeData(List<Recipe> recipes) {
        mRecipes = recipes;
        notifyDataSetChanged();
    }


    public class RecipeAdapterViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.iv_recipe_image) ImageView mIvRecipeImage;
        @BindView(R.id.tv_recipe_name) TextView mTvRecipeName;
        @BindView(R.id.tv_recipe_servings) TextView mTvRecipeServings;

        public RecipeAdapterViewHolder(final View itemView, final Context context) {
            super(itemView);

            ButterKnife.bind(this, itemView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();

                    Recipe recipe = mRecipes.get(pos);

                    Intent intent = new Intent(context, RecipeDetailActivity.class);
                    intent.putExtra(Constants.EXTRA_RECIPE, recipe);
                    context.startActivity(intent);
                }
            });
        }
    }
}
