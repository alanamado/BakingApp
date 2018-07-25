package com.secondfloorapps.bakingapp.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.secondfloorapps.bakingapp.R;
import com.secondfloorapps.bakingapp.fragments.IngredientsAndStepsFragment;
import com.secondfloorapps.bakingapp.models.StepParcelable;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerStepsAdapter extends RecyclerView.Adapter<RecyclerStepsAdapter.MyViewHolder> {

    //--------------------------------------------
    // Variables
    //--------------------------------------------
    private final List<StepParcelable> mStepsList;

    //--------------------------------------------
    // Constructor..
    //--------------------------------------------
    public RecyclerStepsAdapter(Context context, List<StepParcelable> steps){
        mStepsList = steps;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView ShortDescriptions;
        TextView step;
        ImageView stepImage;


        private MyViewHolder(View view) {
            super(view);
            ShortDescriptions = view.findViewById(R.id.tvShortDescription);
            step = view.findViewById(R.id.tvStep);
            stepImage = view.findViewById(R.id.imStepImage);

        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View recipe_steps_view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_steps_item,parent,false);

        return new MyViewHolder(recipe_steps_view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {

        final StepParcelable currentStep;
        final Context activity = holder.itemView.getContext();

        currentStep = mStepsList.get(position);

        //---------------------------------------------------------------
        // populate holder fields..
        //---------------------------------------------------------------
        holder.ShortDescriptions.setText(currentStep.shortDescription);
        if (currentStep.id != 0) {
            holder.step.setText(Integer.toString(currentStep.id));
        }else
        {
           holder.step.setText("");
        }

        //---------------------------------------------------------------
        // Use Picasso to load image from the web into the imageview..
        //---------------------------------------------------------------
        try {
            Picasso.get()
                    .load(mStepsList.get(position).thumbnailURL)
                    .placeholder(R.drawable.ic_ingredients)
                    .into(holder.stepImage, new Callback() {
                        @Override
                        public void onSuccess() {
                        }

                        @Override
                        public void onError(Exception e) {
                            holder.stepImage.setImageResource(R.drawable.ic_steps);
                        }
                    });
        }catch(IllegalArgumentException e)
        {
            holder.stepImage.setImageResource(R.drawable.ic_steps);
        }

        //---------------------------------------------------------------
        // Set up an onclick listener for each step..
        //---------------------------------------------------------------
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                IngredientsAndStepsFragment.OnStepClickListener onStepClickListener = (IngredientsAndStepsFragment.OnStepClickListener) activity;
                onStepClickListener.onStepSelected(currentStep.recipeId, currentStep.id);

            }

        });

    }

    @Override
    public int getItemCount() {
        return mStepsList.size();
    }



}
