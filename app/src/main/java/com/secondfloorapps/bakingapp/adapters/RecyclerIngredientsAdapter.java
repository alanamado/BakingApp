package com.secondfloorapps.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.secondfloorapps.bakingapp.R;
import com.secondfloorapps.bakingapp.models.IngredientParcelable;

import java.util.List;

public class RecyclerIngredientsAdapter extends RecyclerView.Adapter<RecyclerIngredientsAdapter.MyViewHolder> {

    //--------------------------------------------
    // Variables
    //--------------------------------------------
    private final List<IngredientParcelable>  ingredientsList;

    //--------------------------------------------
    // Constructor..
    //--------------------------------------------
    public RecyclerIngredientsAdapter(List<IngredientParcelable> ingredients){ ingredientsList = ingredients; }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView mIngredient;
        TextView mQuantity;
        TextView mMeasure;


        private MyViewHolder(View view) {
            super(view);
            mIngredient = view.findViewById(R.id.tvIngredient);
            mQuantity = view.findViewById(R.id.tvQuantity);
            mMeasure = view.findViewById(R.id.tvMeasure);
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

        IngredientParcelable mCurrentIngredient;

        mCurrentIngredient = ingredientsList.get(position);

        holder.mIngredient.setText(mCurrentIngredient.ingredient);
        holder.mQuantity.setText(mCurrentIngredient.quantity);
        holder.mMeasure.setText(mCurrentIngredient.measure);


    }

    @Override
    public int getItemCount() {
        return ingredientsList.size();
    }



}
