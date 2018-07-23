package com.secondfloorapps.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.secondfloorapps.bakingapp.R;
import com.secondfloorapps.bakingapp.models.Ingredient;
import com.secondfloorapps.bakingapp.models.Ingredient_parc;
import com.secondfloorapps.bakingapp.models.Step_parc;

import java.util.List;

public class adapter_recycler_ingredients extends RecyclerView.Adapter<adapter_recycler_ingredients.MyViewHolder> {

    //--------------------------------------------
    // Variables
    //--------------------------------------------
    private final List<Ingredient_parc>  ingredientsList;

    //--------------------------------------------
    // Constructor..
    //--------------------------------------------
    public adapter_recycler_ingredients(List<Ingredient_parc> ingredients){ ingredientsList = ingredients; }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        final TextView ingredient;
        final TextView quantity;
        final TextView measure;


        private MyViewHolder(View view) {
            super(view);
            ingredient = view.findViewById(R.id.tvIngredient);
            quantity = view.findViewById(R.id.tvQuantity);
            measure = view.findViewById(R.id.tvMeasure);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recipe_ingredients_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_ingredient_item,parent,false);

        return new MyViewHolder(recipe_ingredients_view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        final Ingredient_parc currentIngredient;
        final Context context = holder.itemView.getContext();

        currentIngredient = ingredientsList.get(position);


        holder.ingredient.setText(currentIngredient.ingredient);
        holder.quantity.setText(currentIngredient.quantity);
        holder.measure.setText(currentIngredient.measure);


        //---------------------------------------------------------------
        // Set up an onclick listener for each Ingredient..
        //---------------------------------------------------------------
//        holder.itemView.setOnClickListener(new View.OnClickListener()
//        {
//            @Override
//            public void onClick(View v) {
//
//                Intent i = new Intent(activity, MovieDetailsActivity.class);
//                i.putExtra("Movie", currentMovie);
//                Toast.makeText(context, "Item clicked on", Toast.LENGTH_SHORT).show();
//            }
//
//        });

    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }



}
