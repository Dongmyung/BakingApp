package com.dongmyungahn.android.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.dongmyungahn.android.bakingapp.R;
import com.dongmyungahn.android.bakingapp.model.Recipe;
import com.dongmyungahn.android.bakingapp.utilities.BakingJsonService;
import com.dongmyungahn.android.bakingapp.utilities.Constants;
import com.dongmyungahn.android.bakingapp.utilities.JsonUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Implementation of App Widget functionality.
 */
public class BakingAppWidget extends AppWidgetProvider {
    private static List<Recipe> sRecipes;


    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, ArrayList<Recipe> recipes) {

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_grid_view);

        // Set the GridWidgetService intent to act as the adapter for the GridView
        Intent intent = new Intent(context, GridWidgetService.class);
        intent.putExtra(Constants.EXTRA_WIDGET_DATA, JsonUtils.convertToJsonString(recipes, new TypeToken<ArrayList<Recipe>>() {}.getType()));

        views.setRemoteAdapter(R.id.widget_grid_view, intent);

        // Handle empty gardens
        views.setEmptyView(R.id.widget_grid_view, R.id.empty_view);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    static void updateAppWidgetWithRecipe(Context context, AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds, ArrayList<Recipe> recipes) {

        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, recipes);
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        // Get Recipe Data
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

                // Update Widgets
                updateAppWidgetWithRecipe(context, appWidgetManager, appWidgetIds, recipes);
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                call.cancel();
            }
        });
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

