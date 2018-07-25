package com.secondfloorapps.bakingapp.adapters;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.secondfloorapps.bakingapp.MyApp;
import com.secondfloorapps.bakingapp.R;
import com.secondfloorapps.bakingapp.interfaces.WidgetRecipeChooserInterface;
import com.secondfloorapps.bakingapp.models.Ingredient;
import com.secondfloorapps.bakingapp.models.Ingredient_;
import com.secondfloorapps.bakingapp.models.Recipe;
import com.secondfloorapps.bakingapp.models.Step;
import com.secondfloorapps.bakingapp.models.Step_;
import com.secondfloorapps.bakingapp.widget.WidgetProvider;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class RecyclerRecipeListForWidgetAdapter extends RecyclerView.Adapter<RecyclerRecipeListForWidgetAdapter.MyViewHolder>  {

    //--------------------------------------------
    // Variables
    //--------------------------------------------
    private final List<Recipe> mRecipeList;
    private int mWidgetId;

    //--------------------------------------------
    // Constructor..
    //--------------------------------------------
    public RecyclerRecipeListForWidgetAdapter(Context activity, int widgetId, List<Recipe> recipes)
    {
        mRecipeList = recipes;
        mWidgetId = widgetId;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        final TextView recipeName;
        final ImageView recipeImage;

        private MyViewHolder(View view) {
            super(view);
            recipeName = view.findViewById(R.id.tvRecipeName);
            recipeImage = view.findViewById(R.id.imRecipeImage);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recipe_list_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_recipe_list_item,parent,false);

        return new MyViewHolder(recipe_list_view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Recipe currentRecipe;
        final Context activity = holder.itemView.getContext();

        currentRecipe = mRecipeList.get(position);

        //---------------------------------------------------------------
        // Set up the ObjectBox Boxstore(s)..
        //---------------------------------------------------------------
        MyApp myapp = (MyApp) activity.getApplicationContext();
        BoxStore boxStore = myapp.getBoxStore();
        Box<Ingredient> ingredientBox = boxStore.boxFor(Ingredient.class);
        Box<Step> stepsBox = boxStore.boxFor(Step.class);

        //---------------------------------------------------------------
        // Retrieve the list of ingredients and list of steps..
        //---------------------------------------------------------------
        List<Ingredient> ingredients = ingredientBox.query().equal(Ingredient_.recipeId, currentRecipe.recipeID).build().find();
        List<Step> steps = stepsBox.query().equal(Step_.recipeId, currentRecipe.recipeID).build().find();

        //---------------------------------------------------------------
        // set my holder field values..
        //-------------------------------
        holder.recipeName.setText(currentRecipe.name);

        //---------------------------------------------------------------
        // Use Picasso to load image from the web into the imageview..
        //---------------------------------------------------------------
       try {
           Picasso.get()
                   .load(mRecipeList.get(position).image)
                   .placeholder(R.drawable.ic_ingredients)
                   .into(holder.recipeImage, new Callback() {
                       @Override
                       public void onSuccess() {
                       }

                       @Override
                       public void onError(Exception e) {
                           Toast.makeText(activity,"Error loading an image",Toast.LENGTH_SHORT).show();
                       }
                   });
       }catch(IllegalArgumentException e)
       {
           holder.recipeImage.setImageResource(R.drawable.ic_ingredients);
       }

        //---------------------------------------------------------------
        // store the recipeID chosen to a variable..
        //-------------------------------
        final int recipeID = (int) mRecipeList.get(position).recipeID;

        //---------------------------------------------------------------
        // Set up an onclick listener for each recipe item.
        //---------------------------------------------------------------
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                //---------------------------------------------------------------
                // Call the updateAppWidget method of the widget provider to update this widget..
                //---------------------------------------------------------------
                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(activity);
                WidgetProvider.updateAppWidget(activity, appWidgetManager, mWidgetId, 1);

                //---------------------------------------------------------------
                // call in to the interface  the application implements so we can return the intent results and close the activity..
                //---------------------------------------------------------------
                WidgetRecipeChooserInterface recipe_chooser_interface = (WidgetRecipeChooserInterface) activity;
                recipe_chooser_interface.acceptResult(recipeID);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

}
