package com.dongmyungahn.android.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.dongmyungahn.android.bakingapp.R;
import com.dongmyungahn.android.bakingapp.model.Recipe;
import com.dongmyungahn.android.bakingapp.model.RecipeIngredient;
import com.dongmyungahn.android.bakingapp.utilities.Constants;
import com.dongmyungahn.android.bakingapp.utilities.JsonUtils;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;


public class GridWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        ArrayList<Recipe> recipes = JsonUtils.convertToObject(intent.getStringExtra(Constants.EXTRA_WIDGET_DATA),
                new TypeToken<ArrayList<Recipe>>() {}.getType());

        return new GridRemoteViewFactory(this.getApplicationContext(), recipes);
    }
}

class GridRemoteViewFactory implements RemoteViewsService.RemoteViewsFactory {

    Context mContext;
    List<Recipe> mRecipes;

    public GridRemoteViewFactory(Context applicationContext, List<Recipe> recipes) {
        this.mContext = applicationContext;
        this.mRecipes = recipes;
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if(mRecipes == null) return 0;
        return mRecipes.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        Recipe recipe = mRecipes.get(position);

        RemoteViews remoteViews = new RemoteViews(mContext.getPackageName(), R.layout.widget_recipe_item);

        remoteViews.setTextViewText(R.id.tv_widget_recipe_title,recipe.getName());

        List<RecipeIngredient> ingredients = recipe.getIngredients();
        if(ingredients!=null) {
            StringBuilder builder = new StringBuilder();
            for(RecipeIngredient ingredient : ingredients) {
                builder.append("\u2022 ");
                builder.append(ingredient.getIngredient());
                builder.append(" (");
                builder.append(ingredient.getQuantity());
                builder.append(ingredient.getMeasure());
                builder.append(")\n");
            }
            remoteViews.setTextViewText(R.id.tv_widget_ingredients, builder.toString());
        }

        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
