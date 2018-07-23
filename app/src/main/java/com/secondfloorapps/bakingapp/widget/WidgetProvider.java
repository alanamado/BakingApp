package com.secondfloorapps.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.RemoteViews;

import com.secondfloorapps.bakingapp.MyApp;
import com.secondfloorapps.bakingapp.R;
import com.secondfloorapps.bakingapp.models.Ingredient;
import com.secondfloorapps.bakingapp.models.Ingredient_;
import com.secondfloorapps.bakingapp.models.MyObjectBox;
import com.secondfloorapps.bakingapp.models.Recipe;
import com.secondfloorapps.bakingapp.models.Recipe_;
import com.secondfloorapps.bakingapp.models.Widget;
import com.secondfloorapps.bakingapp.models.Widget_;

import java.util.Calendar;
import java.util.List;


import javax.annotation.ParametersAreNonnullByDefault;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.QueryConsumer;


/**
 * Implementation of MyApp Widget functionality.
 */
public class WidgetProvider extends AppWidgetProvider {

   static final String TAG = "WidgetProvider";


    public static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId, int recipeChosen) {

        // Get widget bundle
        Bundle options = appWidgetManager.getAppWidgetOptions(appWidgetId);
        int width = options.getInt(AppWidgetManager.OPTION_APPWIDGET_MIN_WIDTH);


        // reference ObjectBox database box for the widget
        MyApp myApp = (MyApp)context.getApplicationContext();
        BoxStore boxStore = myApp.getBoxStore();
        Box<Widget> widgetBox = boxStore.boxFor(Widget.class);
        Box<Ingredient> ingredientBox = boxStore.boxFor(Ingredient.class);
        Box<Recipe> recipeBox = boxStore.boxFor(Recipe.class);

        // Create RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_provider);


        //-------------------------------------------------------------------------------------------
        // Search for object in database. If, exists update it's lastTouched date in case I get around to writing a
        // cleanup method.  And if it doesn't exist then add it to the database.
        //-----------------------------------------------------------------------------------------
        Widget widget = widgetBox.query().equal(Widget_.widgetId, appWidgetId).build().findUnique();
        if (widget != null){
            widget.lastTouched = Calendar.getInstance().getTime();
            widget.recipeID = recipeChosen;
            widgetBox.put(widget);
        }else{
            Widget newWidget = new Widget();
            newWidget.widgetId = appWidgetId;
            newWidget.lastTouched = Calendar.getInstance().getTime();
            newWidget.recipeID = recipeChosen;
            widgetBox.put(newWidget);
        }

        //-------------------------------------------------------------------------------------------
        // Get Ingredients from database..
        //-----------------------------------------------------------------------------------------
        String listOfIngredients = "";
        StringBuilder builder = new StringBuilder();

        //  Query for ingredients, given the recipe passed in..
        List<Ingredient> ingredients =  ingredientBox.query().equal(Ingredient_.recipeId, recipeChosen).build().find();
        for (Ingredient i: ingredients) {
            builder.append(" - " + i.ingredient + " (" + i.quantity + " " + i.measure + ")" );
            builder.append(System.getProperty("line.separator"));
        }

        try {
            listOfIngredients = builder.toString();
        }catch (Exception e)
        {
            // do nothing, since listOfIngredients is already initialized to "";
        }

        // Get recipe name
        String recipeName = "";
        Recipe recipe =  recipeBox.query().equal(Recipe_.recipeID, recipeChosen).build().findUnique();
        if (recipeChosen != 0){  recipeName = recipe.name;}



       // Create an intent to launch MainActivity when clicked
       //Intent intent = new Intent(context, MainActivity.class);
       //PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,0);

       // Set up widget to allow click handler to  launch pending intents
       // views.setOnClickPendingIntent(R.id.txClickHere, pendingIntent);


       Log.i(TAG, "Widget ID = " + Integer.toString(appWidgetId));
        views.setTextViewText(R.id.txRecipeName, recipeName);
        views.setTextViewText(R.id.txListOfIngredients, listOfIngredients);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }


    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        int recipeChosen = 0; // default;
        MyApp myApp = (MyApp)context.getApplicationContext();
        BoxStore boxStore = myApp.getBoxStore();
        Box<Widget> widgetBox = boxStore.boxFor(Widget.class);

        // Loops through all widgets on desktop
        for (int appWidgetId : appWidgetIds) {

            // See if a widget with this id is already in the database. Return it.
            Widget widget = widgetBox.query().equal(Widget_.widgetId, appWidgetId).build().findUnique();

            if (widget != null){  // if it doesn't exist add it to database..
                recipeChosen = (int) widget.recipeID;
                widget.lastTouched = Calendar.getInstance().getTime();
                widgetBox.put(widget);
            }else{  // add a new widget to the database..
                Widget newWidget = new Widget();
                newWidget.widgetId = appWidgetId;
                newWidget.lastTouched = Calendar.getInstance().getTime();
                widgetBox.put(newWidget);
            }

          // Call updateAppWidget class..
           updateAppWidget(context, appWidgetManager, appWidgetId, recipeChosen);
           Log.i("WIDGETS", Integer.toString(appWidgetId));
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        MyApp myApp = (MyApp)context.getApplicationContext();
        Box<Widget> widgetBox = myApp.getBoxStore().boxFor(Widget.class);
        for (int appWidgetId : appWidgetIds) {
            Widget widget = widgetBox.query().equal(Widget_.widgetId, appWidgetId).build().findUnique();
            widgetBox.remove(widget);
        }
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

