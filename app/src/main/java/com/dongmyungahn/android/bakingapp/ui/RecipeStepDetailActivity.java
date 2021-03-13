package com.dongmyungahn.android.bakingapp.ui;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.dongmyungahn.android.bakingapp.R;
import com.dongmyungahn.android.bakingapp.model.Recipe;
import com.dongmyungahn.android.bakingapp.model.RecipeStep;
import com.dongmyungahn.android.bakingapp.utilities.Constants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepDetailActivity extends AppCompatActivity {

    private static final String SAVED_INSTANCE_STATE_STEP_POS = "saved_instance_state_step_pos";

    private final int DEFAULT_STEP_POS = 0;

    @Nullable @BindView(R.id.btn_previous) Button mBtnPrevious;
    @Nullable @BindView(R.id.btn_next) Button mBtnNext;


    private RecipeStepDetailFragment mStepDetailFragment;
    private Recipe mRecipe;
    private int mStepPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step_detail);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }


        try {
            mRecipe = (Recipe) intent.getParcelableExtra(Constants.EXTRA_RECIPE);
            if (mRecipe == null) {
                closeOnError();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }


        if(savedInstanceState == null) {
            mStepPos = intent.getIntExtra(Constants.EXTRA_STEP_POS, DEFAULT_STEP_POS);
        } else {
            mStepPos = savedInstanceState.getInt(SAVED_INSTANCE_STATE_STEP_POS);
        }

        RecipeStep step = mRecipe.getSteps().get(mStepPos);

        // Set title
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            if(mRecipe != null) actionBar.setTitle(mRecipe.getName());
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
        }

        FragmentManager fragmentManager = getSupportFragmentManager();

        mStepDetailFragment = (RecipeStepDetailFragment) fragmentManager.findFragmentByTag(Constants.TAG_STEP_DETAIL_FRAGMENT);


        if(mStepDetailFragment == null) {
            // add Recipte Step Detail Fragment
            mStepDetailFragment = new RecipeStepDetailFragment();
            mStepDetailFragment.setStep(step);

            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_detail_fragment_container, mStepDetailFragment, Constants.TAG_STEP_DETAIL_FRAGMENT)
//                    .add(R.id.recipe_step_detail_fragment_container, mStepDetailFragment, TAG_STEP_DETAIL_FRAGMENT)
                    .commit();
        }

        if(mBtnPrevious != null) {
            mBtnPrevious.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mStepPos > 0) {
                        mStepPos--;
                        RecipeStep newStep = mRecipe.getSteps().get(mStepPos);
                        if(newStep != null) {
                            mStepDetailFragment = new RecipeStepDetailFragment();
                            mStepDetailFragment.setStep(newStep);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.recipe_step_detail_fragment_container, mStepDetailFragment, Constants.TAG_STEP_DETAIL_FRAGMENT)
                                    .commit();
                        }
                    } else {
                        Toast.makeText(RecipeStepDetailActivity.this, getString(R.string.no_previous_step), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }

        if(mBtnNext != null) {
            mBtnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mStepPos < (mRecipe.getSteps().size()-1)) {
                        mStepPos++;
                        RecipeStep newStep = mRecipe.getSteps().get(mStepPos);
                        if(newStep != null) {
                            mStepDetailFragment = new RecipeStepDetailFragment();
                            mStepDetailFragment.setStep(newStep);
                            fragmentManager.beginTransaction()
                                    .replace(R.id.recipe_step_detail_fragment_container, mStepDetailFragment, Constants.TAG_STEP_DETAIL_FRAGMENT)
                                    .commit();
                        }
                    } else {
                        Toast.makeText(RecipeStepDetailActivity.this, getString(R.string.no_next_step), Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(SAVED_INSTANCE_STATE_STEP_POS,mStepPos);
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
