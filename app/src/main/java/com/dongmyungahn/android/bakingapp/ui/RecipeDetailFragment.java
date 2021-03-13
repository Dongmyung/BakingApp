package com.dongmyungahn.android.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dongmyungahn.android.bakingapp.R;
import com.dongmyungahn.android.bakingapp.adapter.RecipeIngredientAdapter;
import com.dongmyungahn.android.bakingapp.adapter.RecipeStepAdapter;
import com.dongmyungahn.android.bakingapp.model.Recipe;
import com.dongmyungahn.android.bakingapp.model.RecipeIngredient;
import com.dongmyungahn.android.bakingapp.model.RecipeStep;
import com.dongmyungahn.android.bakingapp.utilities.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeDetailFragment extends Fragment implements RecipeStepAdapter.RecipeStepAdapterOnClickHandler {

    @BindView(R.id.rv_recipe_ingredient) RecyclerView mRvRecipeIngredient;
    @BindView(R.id.rv_recipe_step) RecyclerView mRvRecipeStep;

    private Recipe mRecipe;

    private RecipeIngredientAdapter mIngredientAdapter;
    private RecipeStepAdapter mStepAdapter;


    private boolean mTwoPane;

    public RecipeDetailFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mTwoPane = getResources().getBoolean(R.bool.isTablet);

        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_recipe_detail, container, false);

        ButterKnife.bind(this, rootView);

        mRvRecipeIngredient.setLayoutManager(new LinearLayoutManager(getContext()));
        mIngredientAdapter = new RecipeIngredientAdapter();
        mRvRecipeIngredient.setAdapter(mIngredientAdapter);

        mRvRecipeStep.setLayoutManager(new LinearLayoutManager(getContext()));
        mStepAdapter = new RecipeStepAdapter(this);
        mRvRecipeStep.setAdapter(mStepAdapter);


        if(mRecipe != null) {
            List<RecipeIngredient> ingredients = mRecipe.getIngredients();
            if (ingredients != null && ingredients.size() > 0) {
                mIngredientAdapter.setIngredientData(ingredients);
            }

            List<RecipeStep> steps = mRecipe.getSteps();
            if (steps != null && steps.size() > 0) {
                mStepAdapter.setStepData(steps);
            }
        }

        return rootView;
    }

    public void setRecipe(Recipe recipe) {
        mRecipe = recipe;
    }

    @Override
    public void onClick(int stepPos) {
        if(mTwoPane) {
            RecipeStep newStep = mRecipe.getSteps().get(stepPos);
            if(newStep != null) {
                RecipeStepDetailFragment stepDetailFragment = new RecipeStepDetailFragment();
                stepDetailFragment.setStep(newStep);
                getActivity().getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.recipe_step_detail_fragment_container, stepDetailFragment, Constants.TAG_STEP_DETAIL_FRAGMENT)
                        .commit();
            }
        } else {
            Intent intent = new Intent(getContext(), RecipeStepDetailActivity.class);
            intent.putExtra(Constants.EXTRA_RECIPE, mRecipe);
            intent.putExtra(Constants.EXTRA_STEP_POS, stepPos);
            startActivity(intent);
        }
    }



}
