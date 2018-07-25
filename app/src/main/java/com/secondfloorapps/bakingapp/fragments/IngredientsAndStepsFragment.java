package com.secondfloorapps.bakingapp.fragments;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.secondfloorapps.bakingapp.R;
import com.secondfloorapps.bakingapp.adapters.RecyclerIngredientsAdapter;
import com.secondfloorapps.bakingapp.adapters.RecyclerStepsAdapter;
import com.secondfloorapps.bakingapp.models.IngredientParcelable;
import com.secondfloorapps.bakingapp.models.StepParcelable;

import java.util.ArrayList;

public class IngredientsAndStepsFragment extends Fragment {

    RecyclerView recyclerIngredients;
    RecyclerView recyclerSteps;
    TextView lbIngredients;
   // TextView tvIngredients;
    String mRecipeName;
    Context activity;
    LinearLayoutManager linearLayoutManager_ingredients;
    LinearLayoutManager linearLayoutManager_steps;
    RecyclerIngredientsAdapter adapterIngredients;
    RecyclerStepsAdapter adapterSteps;

    ArrayList<IngredientParcelable> mIngredientsList;
    ArrayList<StepParcelable> mStepsList;

    OnStepClickListener mCallback;

    public interface OnStepClickListener{
        void onStepSelected(long recipeID, int StepID);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        Bundle bundle = getActivity().getIntent().getExtras();
//
//        mRecipeName = bundle.getString("RecipeName");
//        mIngredientsList = bundle.getParcelableArrayList("Ingredients");
//        mStepsList = bundle.getParcelableArrayList("Steps");

        //-----------------------
        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            mRecipeName = bundle.getString("RecipeName");
            mIngredientsList = bundle.getParcelableArrayList("Ingredients");
            mStepsList = bundle.getParcelableArrayList("Steps");
        }
        //---------------------------

        activity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_ingredients_and_steps_list, container,false);

        // adapters
        adapterIngredients = new RecyclerIngredientsAdapter(mIngredientsList);
        adapterSteps = new RecyclerStepsAdapter(activity, mStepsList);

        // references
        lbIngredients = rootView.findViewById(R.id.lbIngredients);
        recyclerIngredients =  rootView.findViewById(R.id.recyclerIngredients);
        recyclerSteps =  rootView.findViewById(R.id.recyclerSteps);

        // Set adapters to recyclerViews
        recyclerIngredients.setAdapter(adapterIngredients);
        recyclerSteps.setAdapter(adapterSteps);

        linearLayoutManager_ingredients = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false);
        linearLayoutManager_steps = new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL,false);

        recyclerIngredients.setLayoutManager(linearLayoutManager_ingredients);
        recyclerSteps.setLayoutManager(linearLayoutManager_steps);

        lbIngredients.setText(mRecipeName + " Ingredients");
        return rootView;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mCallback = (OnStepClickListener) context;
        }catch(ClassCastException e){
            throw new ClassCastException(context.toString() + " must implement OnStepClickListener");
        }
    }

}
