package com.secondfloorapps.bakingapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.secondfloorapps.bakingapp.IngredientsAndStepsActivity;
import com.secondfloorapps.bakingapp.MyApp;
import com.secondfloorapps.bakingapp.R;
import com.secondfloorapps.bakingapp.models.Ingredient;
import com.secondfloorapps.bakingapp.models.Ingredient_;
import com.secondfloorapps.bakingapp.models.Ingredient_parc;
import com.secondfloorapps.bakingapp.models.Recipe;
import com.secondfloorapps.bakingapp.models.Step;
import com.secondfloorapps.bakingapp.models.Step_;
import com.secondfloorapps.bakingapp.models.Step_parc;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class adapter_recycler_recipe_list extends RecyclerView.Adapter<adapter_recycler_recipe_list.MyViewHolder> {

    //--------------------------------------------
    // Variables
    //--------------------------------------------
    private final List<Recipe> recipeList;

    //--------------------------------------------
    // Constructor..
    //--------------------------------------------
    public adapter_recycler_recipe_list(List<Recipe> recipes){ recipeList = recipes; }


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

        final Recipe currentRecipe;
        final Context context = holder.itemView.getContext();
        final ArrayList<Ingredient_parc> ingredient_parcs;
        final ArrayList<Step_parc> step_parcs;
        final MyViewHolder myHolder = holder;

        currentRecipe = recipeList.get(position);

        //---------------------------------------------------------------
        // Set up the ObjectBox Boxstore(s)..
        //---------------------------------------------------------------
        MyApp myapp = (MyApp) context.getApplicationContext();
        BoxStore boxStore = myapp.getBoxStore();
        Box<Ingredient> ingredientBox = boxStore.boxFor(Ingredient.class);
        Box<Step> stepsBox = boxStore.boxFor(Step.class);

        //---------------------------------------------------------------
        // Retrieve the list of ingredients and list of steps..
        //---------------------------------------------------------------
        List<Ingredient> ingredients = ingredientBox.query().equal(Ingredient_.recipeId, currentRecipe.recipeID).build().find();
        List<Step> steps = stepsBox.query().equal(Step_.recipeId, currentRecipe.recipeID).build().find();

        //---------------------------------------------------------------
        // convert my models to models that work with Parcelable, since the ones decorated with ObjectBox annotations don't work with Parcelable well..
        //---------------------------------------------------------------
        ingredient_parcs = returnParcelableVersionOfIngredients(ingredients) ;
        step_parcs = returnParcelableVersionOfSteps(steps);

        //---------------------------------------------------------------
        // set my holder field values..
        //-------------------------------
        myHolder.recipeName.setText(currentRecipe.name);

        //---------------------------------------------------------------
        // Use Picasso to load image from the web into the imageview..
        //---------------------------------------------------------------
       try {
           Picasso.get()
                   .load(recipeList.get(position).image)
                   .placeholder(R.drawable.ic_ingredients)
                   .into(myHolder.recipeImage, new Callback() {
                       @Override
                       public void onSuccess() {
                       }

                       @Override
                       public void onError(Exception e) {
                           Toast.makeText(context,"Error loading an image",Toast.LENGTH_SHORT).show();
                       }
                   });
       }catch(IllegalArgumentException e)
       {
           myHolder.recipeImage.setImageResource(R.drawable.ic_ingredients);
       }


        //---------------------------------------------------------------
        // Set up an onclick listener for each recipe item.
        //---------------------------------------------------------------
        myHolder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(context, IngredientsAndStepsActivity.class);
                i.putExtra("RecipeName",currentRecipe.name);
                i.putParcelableArrayListExtra("Ingredients", ingredient_parcs);
                i.putParcelableArrayListExtra("Steps", step_parcs);

                context.startActivity(i);  // Call the Ingredients and Steps activity.
            }
        });

    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

    public ArrayList<Ingredient_parc> returnParcelableVersionOfIngredients(List<Ingredient> ingredients)
    {
        ArrayList<Ingredient_parc> ingredient_parcs = new ArrayList<>();

        for (Ingredient i: ingredients) {
            Ingredient_parc ingredient_parc = new Ingredient_parc();
            ingredient_parc.uniqueId = i.uniqueId;
            ingredient_parc.ingredient = i.ingredient;
            ingredient_parc.measure = i.measure;
            ingredient_parc.quantity = i.quantity;
            ingredient_parc.recipeId = i.recipeId;

            ingredient_parcs.add(ingredient_parc);
        }

        return ingredient_parcs;
    }

    public ArrayList<Step_parc> returnParcelableVersionOfSteps(List<Step> steps)
    {
        ArrayList<Step_parc> Step_parcs = new ArrayList<>();

        for (Step s: steps) {
            Step_parc steps_parc = new Step_parc();
            steps_parc.uniqueId = s.uniqueId;
            steps_parc.id = s.id;
            steps_parc.shortDescription = s.shortDescription;
            steps_parc.description = s.description;
            steps_parc.thumbnailURL = s.thumbnailURL;
            steps_parc.videoURL = s.videoURL;
            steps_parc.recipeId = s.recipeId;

            Step_parcs.add(steps_parc);
        }

        return Step_parcs;
    }

}
