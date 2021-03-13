package com.dongmyungahn.android.bakingapp.ui;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.dongmyungahn.android.bakingapp.IdlingResource.SimpleIdlingResource;
import com.dongmyungahn.android.bakingapp.R;
import com.dongmyungahn.android.bakingapp.adapter.RecipeAdapter;
import com.dongmyungahn.android.bakingapp.model.Recipe;
import com.dongmyungahn.android.bakingapp.model.RecipeIngredient;
import com.dongmyungahn.android.bakingapp.utilities.BakingJsonService;
import com.dongmyungahn.android.bakingapp.utilities.Constants;
import com.dongmyungahn.android.bakingapp.utilities.NetworkUtils;
import com.dongmyungahn.android.bakingapp.widget.BakingAppWidget;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.rv_recipe_list) RecyclerView mRvRecipes;

    private RecipeAdapter mRecipeAdapter;
    private GridLayoutManager mRvRecipeLayoutManager;

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if(mIdlingResource == null) mIdlingResource = new SimpleIdlingResource();

        return mIdlingResource;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        // Check if it is tablet or not
        boolean isTablet = getResources().getBoolean(R.bool.isTablet);
        int spanCount = 1;
        if(isTablet) {
            spanCount = 3;
        }

        mRvRecipeLayoutManager = new GridLayoutManager(this, spanCount);
        mRvRecipes.setLayoutManager(mRvRecipeLayoutManager);

        mRecipeAdapter = new RecipeAdapter();
        mRvRecipes.setAdapter(mRecipeAdapter);

        getIdlingResource();

//        fetchRecipes();
        fetchRecipes(mIdlingResource);
    }



//    public void fetchRecipes() {
    public void fetchRecipes(@Nullable final SimpleIdlingResource idlingResource) {

        if (idlingResource != null) {
            idlingResource.setIdleState(false);
        }


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        BakingJsonService bakingJsonService = retrofit.create(BakingJsonService.class);

        Call<List<Recipe>> call = bakingJsonService.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                ArrayList<Recipe> recipes = (ArrayList<Recipe>) response.body();
                mRecipeAdapter.setRecipeData(recipes);

                if (idlingResource != null) {
                    idlingResource.setIdleState(true);
                }

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                call.cancel();
            }
        });

    }

}
