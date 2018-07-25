package com.secondfloorapps.bakingapp;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.secondfloorapps.bakingapp.fragments.IngredientsAndStepsFragment;
import com.secondfloorapps.bakingapp.fragments.StepInstructionsFragment;
import com.secondfloorapps.bakingapp.models.IngredientParcelable;
import com.secondfloorapps.bakingapp.models.Step;
import com.secondfloorapps.bakingapp.models.Step_;
import com.secondfloorapps.bakingapp.models.StepParcelable;


import java.util.ArrayList;
import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

public class IngredientsAndStepsActivity extends AppCompatActivity implements IngredientsAndStepsFragment.OnStepClickListener {

    private boolean mTwoPane;
    private LinearLayout step_container;
    BoxStore boxStore;
    String mRecipeName;
    ArrayList<IngredientParcelable> mIngredientsList;
    ArrayList<StepParcelable> mStepsList;
    IngredientsAndStepsFragment ingredientsAndStepsFragment;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString("RecipeName",mRecipeName);
        outState.putParcelableArrayList("Ingredients", mIngredientsList);
        outState.putParcelableArrayList("Steps",mStepsList);
     }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ActivityLaunch", "IngredientsAndStepsActivity");
        setContentView(R.layout.activity_ingredients_and_steps);
       // ingredientsAndStepsFragment_Container = findViewById(R.id.IngredientsAndSteps_Container);

        // Enable Up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        MyApp myApp = (MyApp) this.getApplicationContext();
        boxStore = myApp.getBoxStore();

        if (findViewById(R.id.video_and_step_container) != null) {
            mTwoPane = true;
            step_container = findViewById(R.id.video_and_step_container);

         //   Toast.makeText(this,"In tablet layout",Toast.LENGTH_SHORT).show();

        }else
        {
            mTwoPane = false;
          //  Toast.makeText(this,"In phone layout",Toast.LENGTH_SHORT).show();
        }

        //-------------------------------------
        // get intent extras
        //------------------------------------
        if (savedInstanceState !=null) {
            mRecipeName = savedInstanceState.getString("RecipeName");
            mIngredientsList = savedInstanceState.getParcelableArrayList("Ingredients");
            mStepsList = savedInstanceState.getParcelableArrayList("Steps");
        }else
        {
            Bundle bundle = getIntent().getExtras();
            mRecipeName = bundle.getString("RecipeName");
            mIngredientsList = bundle.getParcelableArrayList("Ingredients");
            mStepsList = bundle.getParcelableArrayList("Steps");
        }
        //-------------------------------------
        // set up fragment bundle..
        //------------------------------------
        Bundle args = new Bundle();
        args.putString("RecipeName",mRecipeName);
        args.putParcelableArrayList("Ingredients",mIngredientsList);
        args.putParcelableArrayList("Steps",mStepsList);

        //-------------------------------------
        // Load up fragment 1..
        //------------------------------------
        FragmentManager fragmentManager = getSupportFragmentManager();
        IngredientsAndStepsFragment thefragment = new IngredientsAndStepsFragment();
        thefragment.setArguments(args);

        //step_container.removeAllViews();
        fragmentManager.beginTransaction().add(R.id.ingredients_and_steps_container, thefragment).commit();

        //-------------------------------------
        // Load up fragment 2..
        //------------------------------------
        if (mTwoPane) {
            StepInstructionsFragment stepInstructionsFragment = new StepInstructionsFragment();
            Bundle args2 = new Bundle();
            StepParcelable p = mStepsList.get(0);
            args2.putParcelable("Step", p);

            step_container.removeAllViews();
            stepInstructionsFragment.setArguments(args2);
            fragmentManager.beginTransaction().add(R.id.video_and_step_container, stepInstructionsFragment).commit();
        }
    }


    @Override
    public void onStepSelected(long recipeID, int StepID) {

        // Enable Up navigation
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        //----------------------------------------------------------
        // Set up ObjectBox Boxstore..
        //---------------------------------------------------------
        Box<Step> stepBox = boxStore.boxFor(Step.class);

        //----------------------------------------------------------
        // Retrieve current Step from the database..
        //---------------------------------------------------------
        Step step = stepBox.query().equal(Step_.recipeId, recipeID).equal(Step_.id, StepID).build().findFirst();

        //----------------------------------------------------------
        // Convert step object to one that implements Parcelable.
        //---------------------------------------------------------
        StepParcelable currentStep = new StepParcelable();
        currentStep.id = step.id;
        currentStep.shortDescription = step.shortDescription;
        currentStep.videoURL = step.videoURL;
        currentStep.thumbnailURL = step.thumbnailURL;
        currentStep.recipeId = step.recipeId;
        currentStep.description = step.description;

        //----------------------------------------------------------
        // if in tablet mode than set up fragments appropriately..
        //---------------------------------------------------------
        if (mTwoPane){
            FragmentManager fragmentManager = getSupportFragmentManager();
            StepInstructionsFragment stepInstructionsFragment = new StepInstructionsFragment();
            Bundle args = new Bundle();
            args.putParcelable("Step",currentStep);

            step_container.removeAllViews();
            stepInstructionsFragment.setArguments(args);
            fragmentManager.beginTransaction().add(R.id.video_and_step_container, stepInstructionsFragment).commit();

       }else {
             Intent i = new Intent(this, InstructionStepsActivity.class);
             i.putExtra("Step",currentStep);
            this.startActivity(i);  // Call the movie details activity.
        }

    }
}
