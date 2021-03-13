package com.dongmyungahn.android.bakingapp.ui;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.dongmyungahn.android.bakingapp.R;
import com.dongmyungahn.android.bakingapp.adapter.RecipeStepAdapter;
import com.dongmyungahn.android.bakingapp.model.Recipe;
import com.dongmyungahn.android.bakingapp.model.RecipeStep;
import com.dongmyungahn.android.bakingapp.utilities.Constants;

public class RecipeDetailActivity extends AppCompatActivity {
    private final String RECYCLERVIEW_STATE_KEY = "recyclerviewStateKey";
    private static final String SAVED_INSTANCE_STATE_STEP_POS = "saved_instance_state_step_pos";

    private final int DEFAULT_STEP_POS = 0;

    private boolean mTwoPane;
    private int mStepPos;

    private RecipeStepDetailFragment mStepDetailFragment;

    private Parcelable mRvState;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        Recipe recipe;
        try {
            recipe = (Recipe) intent.getParcelableExtra(Constants.EXTRA_RECIPE);
            if (recipe == null) {
                closeOnError();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        if(savedInstanceState == null) {
            mStepPos = DEFAULT_STEP_POS;
        } else {
            mStepPos = savedInstanceState.getInt(SAVED_INSTANCE_STATE_STEP_POS);
        }
        RecipeStep step = recipe.getSteps().get(mStepPos);


        // Set title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if(recipe != null) actionBar.setTitle(recipe.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        if(savedInstanceState == null) {
            // add Recipe Detail Fragment
            RecipeDetailFragment detailFragment = new RecipeDetailFragment();
            detailFragment.setRecipe(recipe);

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_detail_fragment_container, detailFragment)
                    .commit();
        }

        // if it is tablet, two pane mode
        mTwoPane = getResources().getBoolean(R.bool.isTablet);
        if(mTwoPane) {
            // Tablet, Two Pane Mode
            if(savedInstanceState == null) {
                mStepDetailFragment = (RecipeStepDetailFragment) fragmentManager.findFragmentByTag(Constants.TAG_STEP_DETAIL_FRAGMENT);

                if(mStepDetailFragment == null) {

                    mStepDetailFragment = new RecipeStepDetailFragment();
                    mStepDetailFragment.setStep(step);

                    fragmentManager.beginTransaction()
                            .replace(R.id.recipe_step_detail_fragment_container, mStepDetailFragment, Constants.TAG_STEP_DETAIL_FRAGMENT)
                            .commit();
                }
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }




}
