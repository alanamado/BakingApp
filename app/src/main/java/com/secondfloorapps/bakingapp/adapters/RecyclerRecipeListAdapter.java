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
import com.secondfloorapps.bakingapp.models.IngredientParcelable;
import com.secondfloorapps.bakingapp.models.Recipe;
import com.secondfloorapps.bakingapp.models.Step;
import com.secondfloorapps.bakingapp.models.Step_;
import com.secondfloorapps.bakingapp.models.StepParcelable;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class RecyclerRecipeListAdapter extends RecyclerView.Adapter<RecyclerRecipeListAdapter.MyViewHolder> {

    //--------------------------------------------
    // Variables
    //--------------------------------------------
    private final List<Recipe> mRecipeList;

    //--------------------------------------------
    // Constructor..
    //--------------------------------------------
    public RecyclerRecipeListAdapter(List<Recipe> recipes){ mRecipeList = recipes; }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mRecipeName;
        ImageView mRecipeImage;

        private MyViewHolder(View view) {
            super(view);
            mRecipeName = view.findViewById(R.id.tvRecipeName);
            mRecipeImage = view.findViewById(R.id.imRecipeImage);
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
        final ArrayList<IngredientParcelable> ingredientParcelable;
        final ArrayList<StepParcelable> stepParcelable;
        final MyViewHolder myHolder = holder;

        currentRecipe = mRecipeList.get(position);

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
        ingredientParcelable = returnParcelableVersionOfIngredients(ingredients) ;
        stepParcelable = returnParcelableVersionOfSteps(steps);

        //---------------------------------------------------------------
        // set my holder field values..
        //-------------------------------
        myHolder.mRecipeName.setText(currentRecipe.name);

        //---------------------------------------------------------------
        // Use Picasso to load image from the web into the imageview..
        //---------------------------------------------------------------
       try {
           Picasso.get()
                   .load(mRecipeList.get(position).image)
                   .placeholder(R.drawable.ic_ingredients)
                   .into(myHolder.mRecipeImage, new Callback() {
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
           myHolder.mRecipeImage.setImageResource(R.drawable.ic_ingredients);
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
                i.putParcelableArrayListExtra("Ingredients", ingredientParcelable);
                i.putParcelableArrayListExtra("Steps", stepParcelable);

                context.startActivity(i);  // Call the Ingredients and Steps activity.
            }
        });

    }

    @Override
    public int getItemCount() {
        return mRecipeList.size();
    }

    public ArrayList<IngredientParcelable> returnParcelableVersionOfIngredients(List<Ingredient> ingredients)
    {
        ArrayList<IngredientParcelable> ingredientParcelablesList = new ArrayList<>();

        for (Ingredient i: ingredients) {
            IngredientParcelable ingredientParcelable = new IngredientParcelable();
            ingredientParcelable.uniqueId = i.uniqueId;
            ingredientParcelable.ingredient = i.ingredient;
            ingredientParcelable.measure = i.measure;
            ingredientParcelable.quantity = i.quantity;
            ingredientParcelable.recipeId = i.recipeId;

            ingredientParcelablesList.add(ingredientParcelable);
        }

        return ingredientParcelablesList;
    }

    public ArrayList<StepParcelable> returnParcelableVersionOfSteps(List<Step> steps)
    {
        ArrayList<StepParcelable> stepParcelablesList = new ArrayList<>();

        for (Step s: steps) {
            StepParcelable steps_parc = new StepParcelable();
            steps_parc.uniqueId = s.uniqueId;
            steps_parc.id = s.id;
            steps_parc.shortDescription = s.shortDescription;
            steps_parc.description = s.description;
            steps_parc.thumbnailURL = s.thumbnailURL;
            steps_parc.videoURL = s.videoURL;
            steps_parc.recipeId = s.recipeId;

            stepParcelablesList.add(steps_parc);
        }

        return stepParcelablesList;
    }

}
