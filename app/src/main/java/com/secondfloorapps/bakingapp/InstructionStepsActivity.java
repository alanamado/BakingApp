package com.secondfloorapps.bakingapp;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.secondfloorapps.bakingapp.fragments.Fragment_Step_Instructions;
import com.secondfloorapps.bakingapp.fragments.Fragment_Step_Navigation;
import com.secondfloorapps.bakingapp.interfaces.interface_for_step_navigation;
import com.secondfloorapps.bakingapp.models.Step;
import com.secondfloorapps.bakingapp.models.Step_;
import com.secondfloorapps.bakingapp.models.Step_parc;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class InstructionStepsActivity extends AppCompatActivity implements interface_for_step_navigation {

   // boolean mSinglePane;
    BoxStore boxStore;
    Box<Step> stepBox;
    FragmentManager fragmentManager ;
    long mCurrentRecipeID;
    int mCurrentStepID;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_step_instructions);

        //----------------------------------------------------------
        // Reference ObjectBox boxstore..
        //---------------------------------------------------------

        MyApp myApp = (MyApp) this.getApplicationContext();
        boxStore = myApp.getBoxStore();

        //----------------------------------------------------------
        // Set up stepbox..
        //---------------------------------------------------------
        stepBox = boxStore.boxFor(Step.class);


        //-----------------------------------------------------
        // Get retrieve Step from Extra passed with this intent.
        //-----------------------------------------------------
        Intent i = getIntent();
        Step_parc currentStep = i.getParcelableExtra("Step");

        mCurrentRecipeID = currentStep.recipeId;
        mCurrentStepID  = currentStep.id;


        // Set up fragment manager..
        fragmentManager = getSupportFragmentManager();

        // Set up bundle
        Bundle args =  new Bundle();
        args.putParcelable("Step",currentStep);

        //---------------------------------------------------------------------------------------------------
        // Add the bundle to the  Step_Instructions fragment and then add the fragment to it's container
        //--------------------------------------------------------------------------------------------------
        Fragment_Step_Instructions step_instructions = new Fragment_Step_Instructions();
        step_instructions.setArguments(args);

        // load up the steps fragment into the Framelayout that was set up for it in this activity's layout
        fragmentManager.beginTransaction()
                .add(R.id.instructions_container,step_instructions,"instruction_fragment")
                .commit();

        //-----------------------------------------------------
        // Determine if where in a phone layout..
        //-----------------------------------------------------
        if (findViewById(R.id.navigation_container) != null) {
            //mSinglePane = true;

            //---------------------------------------------------------------------------------------------------
            // Add the bundle to the  Step_Instructions fragment and then add the fragment to it's container
            //--------------------------------------------------------------------------------------------------
            Fragment_Step_Navigation step_navigation = new Fragment_Step_Navigation();
            step_navigation.setArguments(args);

            // load up the navigation fragment into the Framelayout that was set up for it in this activity's layout
            fragmentManager.beginTransaction()
                    .add(R.id.navigation_container,step_navigation,"navigation_fragment")
                    .commit();

        }
    }


    @Override
    public void navigatePrevious() {
        //----------------------------------------------------------
        // Retrieve previous Step from the database if it exists..
        //---------------------------------------------------------
        if (this.mCurrentStepID != 0) {
            int previousStepId = this.mCurrentStepID - 1;
            Step previousStep = stepBox.query().equal(Step_.recipeId, this.mCurrentRecipeID).equal(Step_.id, previousStepId).build().findFirst();
            Step_parc previousStep_parc;  // parcelable version of model.

            if (previousStep != null){

                this.mCurrentRecipeID = previousStep.recipeId;
                this.mCurrentStepID = previousStep.id;

                previousStep_parc =  new Step_parc();
                previousStep_parc.recipeId = previousStep.recipeId;
                previousStep_parc.id = previousStep.id;
                previousStep_parc.description = previousStep.description;
                previousStep_parc.thumbnailURL = previousStep.thumbnailURL;
                previousStep_parc.videoURL = previousStep.videoURL;
                previousStep_parc.shortDescription = previousStep.shortDescription;
                previousStep_parc.uniqueId = previousStep.uniqueId;


                // Set up bundle
                Bundle args =  new Bundle();
                args.putParcelable("Step",previousStep_parc);

                //---------------------------------------------------------------------------------------------------
                // Add the bundle to the  Step_Instructions fragment and then add the fragment to it's container
                //--------------------------------------------------------------------------------------------------
                Fragment_Step_Instructions step_instructions = new Fragment_Step_Instructions();
                step_instructions.setArguments(args);


                // load up the steps fragment into the Framelayout that was set up for it in this activity's layout
                fragmentManager.beginTransaction()
                        .replace(R.id.instructions_container,step_instructions,"instruction_fragment")
                        .commit();
            }
        }

        //Toast.makeText(this,"Previous button clicked",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateNext() {

        Toast.makeText(this,"next button clicked",Toast.LENGTH_SHORT).show();
        //----------------------------------------------------------
        // Retrieve previous Step from the database if it exists..
        //---------------------------------------------------------
            int nextStepId = this.mCurrentStepID + 1;
            Step nextStep = stepBox.query().equal(Step_.recipeId, this.mCurrentRecipeID).equal(Step_.id, nextStepId).build().findFirst();
            Step_parc nextStep_parc;  // parcelable version of model.

            if (nextStep != null){

                this.mCurrentRecipeID = nextStep.recipeId;
                this.mCurrentStepID = nextStep.id;

                nextStep_parc =  new Step_parc();
                nextStep_parc.recipeId = nextStep.recipeId;
                nextStep_parc.id = nextStep.id;
                nextStep_parc.description = nextStep.description;
                nextStep_parc.thumbnailURL = nextStep.thumbnailURL;
                nextStep_parc.videoURL = nextStep.videoURL;
                nextStep_parc.shortDescription = nextStep.shortDescription;
                nextStep_parc.uniqueId = nextStep.uniqueId;


                // Set up bundle
                Bundle args =  new Bundle();
                args.putParcelable("Step",nextStep_parc);

                //---------------------------------------------------------------------------------------------------
                // Add the bundle to the  Step_Instructions fragment and then add the fragment to it's container
                //--------------------------------------------------------------------------------------------------
                Fragment_Step_Instructions step_instructions = new Fragment_Step_Instructions();
                step_instructions.setArguments(args);


                // load up the steps fragment into the Framelayout that was set up for it in this activity's layout
                fragmentManager.beginTransaction()
                        .replace(R.id.instructions_container,step_instructions,"instruction_fragment")
                        .commit();
            }


    }
}
