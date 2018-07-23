package com.secondfloorapps.bakingapp;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.secondfloorapps.bakingapp.fragments.Fragment_Ingredients_And_Steps;
import com.secondfloorapps.bakingapp.fragments.Fragment_Step_Instructions;
import com.secondfloorapps.bakingapp.models.Recipe;
import com.secondfloorapps.bakingapp.models.Step;
import com.secondfloorapps.bakingapp.models.Step_;
import com.secondfloorapps.bakingapp.models.Step_parc;



import io.objectbox.Box;
import io.objectbox.BoxStore;

public class IngredientsAndStepsActivity extends AppCompatActivity implements Fragment_Ingredients_And_Steps.OnStepClickListener {

    private boolean mTwoPane;
    private LinearLayout step_container;
    BoxStore boxStore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients_and_steps);

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
    }


    @Override
    public void onStepSelected(long recipeID, int StepID) {

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
        Step_parc currentStep = new Step_parc();
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
            Fragment_Step_Instructions fragment_step_instructions = new Fragment_Step_Instructions();
            Bundle args = new Bundle();
            args.putParcelable("Step",currentStep);

            step_container.removeAllViews();
            fragment_step_instructions.setArguments(args);
            fragmentManager.beginTransaction().add(R.id.video_and_step_container, fragment_step_instructions).commit();

       }else {
             Intent i = new Intent(this, InstructionStepsActivity.class);
             i.putExtra("Step",currentStep);
            this.startActivity(i);  // Call the movie details activity.
        }

    }
}
