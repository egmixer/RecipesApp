package com.example.recipesapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.recipesapp.R;
import com.example.recipesapp.webservice.WebService;
import com.example.recipesapp.webservice.models.RecipeResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipesWidgetService extends RemoteViewsService {
    private final String TAG = RecipesWidgetService.class.getSimpleName();

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeWidgetFactory(this.getApplicationContext());
    }

    class RecipeWidgetFactory implements RemoteViewsFactory {

        private Context context;
        List<RecipeResponse> recipeResponseArrayList = new ArrayList<>();
        private AppWidgetManager appWidgetManager;
        private int[] widgetId;

        public RecipeWidgetFactory(Context context) {
            this.context = context;
            ComponentName componentName = new ComponentName(context, RecipeWidget.class);
            appWidgetManager = AppWidgetManager.getInstance(context);
            widgetId = appWidgetManager.getAppWidgetIds(componentName);
        }

        @Override
        public void onCreate() {
            WebService webService = WebService.getInstance();
            Call<List<RecipeResponse>> recipeResponseCall = webService.getServices().getRecipes();
            recipeResponseCall.enqueue(new Callback<List<RecipeResponse>>() {
                @Override
                public void onResponse(Call<List<RecipeResponse>> call, Response<List<RecipeResponse>> response) {
                    if (response.isSuccessful()) {
                        recipeResponseArrayList = response.body();
                        appWidgetManager.notifyAppWidgetViewDataChanged(widgetId, R.id.recipes_sv);
                    } else {

                    }
                }

                @Override
                public void onFailure(Call<List<RecipeResponse>> call, Throwable t) {

                }
            });
        }

        @Override
        public void onDataSetChanged() {

        }

        @Override
        public void onDestroy() {
            recipeResponseArrayList.clear();
        }

        @Override
        public int getCount() {
            return recipeResponseArrayList.size();
        }

        @Override
        public RemoteViews getViewAt(int i) {
            if (recipeResponseArrayList == null || recipeResponseArrayList.size() == 0) return null;
            RecipeResponse recipeResponse = recipeResponseArrayList.get(i);
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.item_recipe_widget);
            String recipeTitle = recipeResponse.getName();
            views.setTextViewText(R.id.recipe_title_tv, recipeTitle);
            Bundle extras = new Bundle();
            extras.putParcelable("recipe", recipeResponse);
            Intent intent = new Intent();
            intent.putExtras(extras);
            views.setOnClickFillInIntent(R.id.recipe_title_tv, intent);
            return views;
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
}
