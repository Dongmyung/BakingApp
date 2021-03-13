package com.dongmyungahn.android.bakingapp.utilities;

import com.dongmyungahn.android.bakingapp.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface BakingJsonService {
    @GET("baking.json")
    Call<List<Recipe>> getRecipes();
}
