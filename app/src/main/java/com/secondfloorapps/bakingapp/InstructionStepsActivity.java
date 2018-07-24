package com.secondfloorapps.bakingapp;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.secondfloorapps.bakingapp.fragments.StepInstructionsFragment;
import com.secondfloorapps.bakingapp.fragments.StepNavigationFragment;
import com.secondfloorapps.bakingapp.interfaces.StepNavigationInterface;
import com.secondfloorapps.bakingapp.models.Step;
import com.secondfloorapps.bakingapp.models.Step_;
import com.secondfloorapps.bakingapp.models.StepParcelable;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class InstructionStepsActivity extends AppCompatActivity implements StepNavigationInterface {

   // boolean mSinglePane;
    BoxStore boxStore;
    Box<Step> stepBox;
    FragmentManager fragmentManager ;
    long mCurrentRecipeID;
    int mCurrentStepID;
    StepInstructionsFragment step_instructions;
    StepNavigationFragment step_navigation;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        getSupportFragmentManager().putFragment(outState, "instruction_fragment", step_instructions);
         getSupportFragmentManager().putFragment(outState, "navigation_fragment", step_navigation);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ActivityLaunch", "InstructionStepsActivity");
        setContentView(R.layout.activity_fragment_step_instructions);

        // Enable Up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState != null) {
            //Restore the fragment's instance
            step_instructions = (StepInstructionsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "instruction_fragment");
            step_navigation = (StepNavigationFragment) getSupportFragmentManager().getFragment(savedInstanceState, "navigation_fragment");
        }

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
        StepParcelable currentStep = i.getParcelableExtra("Step");

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
        if (step_navigation == null) {
            step_instructions = new StepInstructionsFragment();
            step_instructions.setArguments(args);

            // load up the steps fragment into the Framelayout that was set up for it in this activity's layout
            fragmentManager.beginTransaction()
                    .add(R.id.instructions_container, step_instructions, "instruction_fragment")
                    .commit();
        }else{
            // load up the steps fragment into the Framelayout that was set up for it in this activity's layout
            fragmentManager.beginTransaction()
                    .replace(R.id.instructions_container, step_instructions, "instruction_fragment")
                    .commit();
        }
        //-----------------------------------------------------
        // Determine if where in a phone layout..
        //-----------------------------------------------------
        if (findViewById(R.id.navigation_container) != null) {
            //mSinglePane = true;

            //---------------------------------------------------------------------------------------------------
            // Add the bundle to the  Step_Instructions fragment and then add the fragment to it's container
            //--------------------------------------------------------------------------------------------------
            if (step_navigation == null) {
                step_navigation = new StepNavigationFragment();
                step_navigation.setArguments(args);

                // load up the navigation fragment into the Framelayout that was set up for it in this activity's layout
                fragmentManager.beginTransaction()
                        .add(R.id.navigation_container,step_navigation,"navigation_fragment")
                        .commit();

            }else{
                //step_navigation.setArguments(args);

                // load up the navigation fragment into the Framelayout that was set up for it in this activity's layout
                fragmentManager.beginTransaction()
                        .replace(R.id.navigation_container,step_navigation,"navigation_fragment")
                        .commit();
            }

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
            StepParcelable previousStepParcelable;  // parcelable version of model.

            if (previousStep != null){

                this.mCurrentRecipeID = previousStep.recipeId;
                this.mCurrentStepID = previousStep.id;

                previousStepParcelable =  new StepParcelable();
                previousStepParcelable.recipeId = previousStep.recipeId;
                previousStepParcelable.id = previousStep.id;
                previousStepParcelable.description = previousStep.description;
                previousStepParcelable.thumbnailURL = previousStep.thumbnailURL;
                previousStepParcelable.videoURL = previousStep.videoURL;
                previousStepParcelable.shortDescription = previousStep.shortDescription;
                previousStepParcelable.uniqueId = previousStep.uniqueId;


                // Set up bundle
                Bundle args =  new Bundle();
                args.putParcelable("Step", previousStepParcelable);

                //---------------------------------------------------------------------------------------------------
                // Add the bundle to the  Step_Instructions fragment and then add the fragment to it's container
                //--------------------------------------------------------------------------------------------------
                StepInstructionsFragment step_instructions = new StepInstructionsFragment();
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
            StepParcelable nextStepParcelable;  // parcelable version of model.

            if (nextStep != null){

                this.mCurrentRecipeID = nextStep.recipeId;
                this.mCurrentStepID = nextStep.id;

                nextStepParcelable =  new StepParcelable();
                nextStepParcelable.recipeId = nextStep.recipeId;
                nextStepParcelable.id = nextStep.id;
                nextStepParcelable.description = nextStep.description;
                nextStepParcelable.thumbnailURL = nextStep.thumbnailURL;
                nextStepParcelable.videoURL = nextStep.videoURL;
                nextStepParcelable.shortDescription = nextStep.shortDescription;
                nextStepParcelable.uniqueId = nextStep.uniqueId;


                // Set up bundle
                Bundle args =  new Bundle();
                args.putParcelable("Step", nextStepParcelable);

                //---------------------------------------------------------------------------------------------------
                // Add the bundle to the  Step_Instructions fragment and then add the fragment to it's container
                //--------------------------------------------------------------------------------------------------
                StepInstructionsFragment step_instructions = new StepInstructionsFragment();
                step_instructions.setArguments(args);


                // load up the steps fragment into the Framelayout that was set up for it in this activity's layout
                fragmentManager.beginTransaction()
                        .replace(R.id.instructions_container,step_instructions,"instruction_fragment")
                        .commit();
            }


    }
}
