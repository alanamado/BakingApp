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
    StepParcelable mCurrentStep;
    StepInstructionsFragment mStepInstructionsFragment;
    StepNavigationFragment mStepNavigationFragment;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //Save the fragment's instance
        if (mStepInstructionsFragment != null) {
            getSupportFragmentManager().putFragment(outState, "instruction_fragment", mStepInstructionsFragment);
          }
        if (mStepNavigationFragment != null) {
            getSupportFragmentManager().putFragment(outState, "navigation_fragment", mStepNavigationFragment);
        }
        outState.putLong("mCurrentRecipeID",mCurrentRecipeID);
        outState.putInt("mCurrentStepID",mCurrentStepID);
        outState.putParcelable("mCurrentStep",mCurrentStep);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ActivityLaunch", "InstructionStepsActivity");
        setContentView(R.layout.activity_fragment_step_instructions);

        // Enable Up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //----------------------------------------------------------
        // Reference ObjectBox boxstore..
        //---------------------------------------------------------

        MyApp myApp = (MyApp) this.getApplicationContext();
        boxStore = myApp.getBoxStore();


        if (savedInstanceState != null) {
            //Restore the fragment's instance
            mStepInstructionsFragment = (StepInstructionsFragment) getSupportFragmentManager().getFragment(savedInstanceState, "instruction_fragment");
            mStepNavigationFragment = (StepNavigationFragment) getSupportFragmentManager().getFragment(savedInstanceState, "navigation_fragment");
            mCurrentRecipeID = savedInstanceState.getLong("mCurrentRecipeID");
            mCurrentStepID = savedInstanceState.getInt("mCurrentStepID");
            mCurrentStep = savedInstanceState.getParcelable("mCurrentStep");
        }else
        {
            //-----------------------------------------------------
            // Get retrieve Step from Extra passed with this intent.
            //-----------------------------------------------------
            Intent i = getIntent();
            mCurrentStep = i.getParcelableExtra("Step");
            mCurrentRecipeID = mCurrentStep.recipeId;
            mCurrentStepID  = mCurrentStep.id;

            //----------------------------------------------------------
            // Set up stepbox..
            //---------------------------------------------------------
            stepBox = boxStore.boxFor(Step.class);
            Step currentStep = stepBox.query().equal(Step_.recipeId, this.mCurrentRecipeID).equal(Step_.id, this.mCurrentStepID).build().findFirst();

            StepParcelable mCurrentStepID = new StepParcelable();
            mCurrentStepID.recipeId = currentStep.recipeId;
            mCurrentStepID.id = currentStep.id;
            mCurrentStepID.description = currentStep.description;
            mCurrentStepID.thumbnailURL = currentStep.thumbnailURL;
            mCurrentStepID.videoURL = currentStep.videoURL;
            mCurrentStepID.shortDescription = currentStep.shortDescription;
            mCurrentStepID.uniqueId = currentStep.uniqueId;
        }




        // Set up fragment manager..
        fragmentManager = getSupportFragmentManager();

        // Set up bundle
        Bundle args =  new Bundle();
        args.putParcelable("Step",mCurrentStep);

        //---------------------------------------------------------------------------------------------------
        // Add the bundle to the  Step_Instructions fragment and then add the fragment to it's container
        //--------------------------------------------------------------------------------------------------
        if (mStepInstructionsFragment == null) {
            mStepInstructionsFragment = new StepInstructionsFragment();
            mStepInstructionsFragment.setArguments(args);

            // load up the steps fragment into the Framelayout that was set up for it in this activity's layout
            fragmentManager.beginTransaction()
                    .add(R.id.instructions_container, mStepInstructionsFragment, "instruction_fragment")
                    .commit();
        }else{
            // load up the steps fragment into the Framelayout that was set up for it in this activity's layout
            fragmentManager.beginTransaction()
                    .replace(R.id.instructions_container, mStepInstructionsFragment, "instruction_fragment")
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
            if (mStepNavigationFragment == null) {
                mStepNavigationFragment = new StepNavigationFragment();
                mStepNavigationFragment.setArguments(args);

                // load up the navigation fragment into the Framelayout that was set up for it in this activity's layout
                fragmentManager.beginTransaction()
                        .add(R.id.navigation_container,mStepNavigationFragment,"navigation_fragment")
                        .commit();

            }else{
                //step_navigation.setArguments(args);

                // load up the navigation fragment into the Framelayout that was set up for it in this activity's layout
                fragmentManager.beginTransaction()
                        .replace(R.id.navigation_container,mStepNavigationFragment,"navigation_fragment")
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
            int previousStepId = mCurrentStep.id - 1;
            stepBox = boxStore.boxFor(Step.class);
            Step previousStep = stepBox.query().equal(Step_.recipeId, this.mCurrentRecipeID).equal(Step_.id, previousStepId).build().findFirst();
            StepParcelable previousStepParcelable;  // parcelable version of model.

            if (previousStep != null){


                previousStepParcelable =  new StepParcelable();
                previousStepParcelable.recipeId = previousStep.recipeId;
                previousStepParcelable.id = previousStep.id;
                previousStepParcelable.description = previousStep.description;
                previousStepParcelable.thumbnailURL = previousStep.thumbnailURL;
                previousStepParcelable.videoURL = previousStep.videoURL;
                previousStepParcelable.shortDescription = previousStep.shortDescription;
                previousStepParcelable.uniqueId = previousStep.uniqueId;

                this.mCurrentRecipeID = previousStep.recipeId;
                this.mCurrentStepID = previousStep.id;
                this.mCurrentStep = previousStepParcelable;

                // Set up bundle
                Bundle args =  new Bundle();
                args.putParcelable("Step", mCurrentStep);

                //---------------------------------------------------------------------------------------------------
                // Add the bundle to the  Step_Instructions fragment and then add the fragment to it's container
                //--------------------------------------------------------------------------------------------------
                mStepInstructionsFragment = new StepInstructionsFragment();
                mStepInstructionsFragment.setArguments(args);


                // load up the steps fragment into the Framelayout that was set up for it in this activity's layout
                fragmentManager.beginTransaction()
                        .replace(R.id.instructions_container,mStepInstructionsFragment,"instruction_fragment")
                        .commit();
            }
        }

        //Toast.makeText(this,"Previous button clicked",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void navigateNext() {

        //----------------------------------------------------------
        // Retrieve previous Step from the database if it exists..
        //---------------------------------------------------------
            int nextStepId = this.mCurrentStep.id + 1;
            stepBox = boxStore.boxFor(Step.class);
            Step nextStep = stepBox.query().equal(Step_.recipeId, this.mCurrentRecipeID).equal(Step_.id, nextStepId).build().findFirst();
            StepParcelable nextStepParcelable;  // parcelable version of model.

            if (nextStep != null){
               // Toast.makeText(this,"next button clicked",Toast.LENGTH_SHORT).show();

                nextStepParcelable =  new StepParcelable();
                nextStepParcelable.recipeId = nextStep.recipeId;
                nextStepParcelable.id = nextStep.id;
                nextStepParcelable.description = nextStep.description;
                nextStepParcelable.thumbnailURL = nextStep.thumbnailURL;
                nextStepParcelable.videoURL = nextStep.videoURL;
                nextStepParcelable.shortDescription = nextStep.shortDescription;
                nextStepParcelable.uniqueId = nextStep.uniqueId;

                this.mCurrentRecipeID = nextStep.recipeId;
                this.mCurrentStepID = nextStep.id;
                this.mCurrentStep = nextStepParcelable;

                // Set up bundle
                Bundle args =  new Bundle();
                args.putParcelable("Step", mCurrentStep);

                //---------------------------------------------------------------------------------------------------
                // Add the bundle to the  Step_Instructions fragment and then add the fragment to it's container
                //--------------------------------------------------------------------------------------------------
                mStepInstructionsFragment = new StepInstructionsFragment();
                mStepInstructionsFragment.setArguments(args);


                // load up the steps fragment into the Framelayout that was set up for it in this activity's layout
                fragmentManager.beginTransaction()
                        .replace(R.id.instructions_container,mStepInstructionsFragment,"instruction_fragment")
                        .commit();
            }


    }
}
