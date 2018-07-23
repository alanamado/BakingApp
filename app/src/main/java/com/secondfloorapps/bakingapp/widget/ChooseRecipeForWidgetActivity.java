package com.secondfloorapps.bakingapp.widget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.secondfloorapps.bakingapp.MyApp;
import com.secondfloorapps.bakingapp.R;
import com.secondfloorapps.bakingapp.adapters.adapter_recycler_recipe_list_for_widget;
import com.secondfloorapps.bakingapp.interfaces.interface_widget_recipe_chooser;
import com.secondfloorapps.bakingapp.models.Recipe;

import java.util.List;

import io.objectbox.BoxStore;

public class ChooseRecipeForWidgetActivity extends AppCompatActivity  implements interface_widget_recipe_chooser {

    private final Context context = this;
    private int widgetId;
    private adapter_recycler_recipe_list_for_widget adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_recipe_for_widget);

        recyclerView = findViewById(R.id.rcRecipes);

        //---------------------------------------------------------
        // Get the widget's id
        //-------------------------------------------------------------
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        if (extras != null) {
            widgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);

            if (widgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                finish(); // exit out activity.
            }
        }

        //----------------------------------------------------------
        // Set up ObjectBox's Boxstore(s)..
        //---------------------------------------------------------
        MyApp myapp = (MyApp) context.getApplicationContext();
        BoxStore boxStore = myapp.getBoxStore();
        List<Recipe> recipeList = boxStore.boxFor(Recipe.class).getAll();

        //----------------------------------------------------------
        // Set up recyclerview and its adapter.
        //---------------------------------------------------------
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new adapter_recycler_recipe_list_for_widget(this,widgetId, recipeList);
        recyclerView.setAdapter(adapter);

    }

    @Override
    public void acceptResult(int recipeId) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        WidgetProvider.updateAppWidget(context, appWidgetManager, widgetId, recipeId);

        Intent resultValue = new Intent();
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetId);
        setResult(RESULT_OK, resultValue);
        finish();// exit out activity.
    }
}
