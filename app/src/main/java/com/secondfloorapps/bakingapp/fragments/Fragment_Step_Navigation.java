package com.secondfloorapps.bakingapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.secondfloorapps.bakingapp.R;
import com.secondfloorapps.bakingapp.interfaces.interface_for_step_navigation;
import com.secondfloorapps.bakingapp.models.Step_parc;

public class Fragment_Step_Navigation extends Fragment {

    Step_parc currentStep;
    Button btNextStep;
    Button btPreviousStep;
    Context activity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = this.getArguments();
        if (bundle != null)
        {
            currentStep = bundle.getParcelable("Step");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_step_navigation, container,false);

        activity = getContext();

        btNextStep = rootView.findViewById(R.id.btNextStep);
        btPreviousStep = rootView.findViewById(R.id.btPreviousStep);

        //--------------------------------------------------------
        // Next step button click
        //---------------------------------------------------------
        btPreviousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interface_for_step_navigation step_navigation = (interface_for_step_navigation) activity;
                step_navigation.navigatePrevious();
             //  Toast.makeText(context,"Next button clicked",Toast.LENGTH_SHORT).show();
            }
        });

        //--------------------------------------------------------
        // Next previous button click
        //---------------------------------------------------------
        btNextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                interface_for_step_navigation step_navigation = (interface_for_step_navigation) activity;
                step_navigation.navigateNext();
               // Toast.makeText(context,"Previous button clicked",Toast.LENGTH_SHORT).show();
            }
        });

        return rootView;
    }
}
