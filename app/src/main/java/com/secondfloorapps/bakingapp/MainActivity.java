package com.secondfloorapps.bakingapp;

import android.content.Context;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.secondfloorapps.bakingapp.fragments.RecipesListFragment;
import com.secondfloorapps.bakingapp.models.Ingredient;
import com.secondfloorapps.bakingapp.models.Recipe;
import com.secondfloorapps.bakingapp.models.Step;

import com.secondfloorapps.bakingapp.models.Widget;
import com.secondfloorapps.bakingapp.retrofit.RecipeService;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;

import okhttp3.OkHttpClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    Context context;
    LinearLayout fragment_container_layout;


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // outState.putParcelableArrayList("RecipeList", this.MovieList);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("ActivityLaunch", "MainActivity");
        setContentView(R.layout.activity_main);

        //---------------------------------
        // set up references.
        //---------------------------------
        fragment_container_layout = findViewById(R.id.fragment_container);
        context = this;

        //---------------------------------
        // Download recipes..
        //---------------------------------
        downloadRecipes();
    }

    public void downloadRecipes() {
        MyApp myApp = (MyApp)getApplicationContext();
        BoxStore boxStore = myApp.getBoxStore();

        final Box<Recipe> recipeBox = boxStore.boxFor(Recipe.class);
        final Box<Ingredient> ingredientsBox = boxStore.boxFor(Ingredient.class);
        final Box<Step> stepsBox = boxStore.boxFor(Step.class);
        final Box<Widget> widgetBox = boxStore.boxFor(Widget.class);
        final Context activity = context;

        //---------------------------------
        // Clear out existing data..
        //---------------------------------
        recipeBox.removeAll();
        ingredientsBox.removeAll();
        stepsBox.removeAll();
        // widgetBox.removeAll();

        //--------------------------------------
        // Create new OkHttp client..
        //--------------------------------------
        OkHttpClient client = new OkHttpClient();

        //---------------------------------------------------------
        // Register OkHttpClient to Espresso Idling registry only
        // in debug mode..
        //-----------------------------------------------------
        if (BuildConfig.DEBUG) {
            IdlingResources.registerOkHttp(client);
        }

        //--------------------------------------
        // Build Retrofit call..
        //--------------------------------------
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://d17h27t6h515a5.cloudfront.net/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        //--------------------------------------
        // Make call to download recipes..
        //--------------------------------------
        RecipeService service = retrofit.create(RecipeService.class);

        service.getRecipes().enqueue(new Callback<List<Recipe>>(){
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response){


                List<Recipe> Recipes = response.body();

                //-----------------------------------------------------------
                // Loop through results and add data to ObjectBox database..
                //-----------------------------------------------------------
                for (Recipe r : Recipes) {
                    Recipe recipe = new Recipe();
                    recipe.recipeID = r.id;
                    recipe.name = r.name;
                    recipe.servings = r.servings;
                    recipe.image = r.image;
                    recipe.ingredients = r.ingredients;
                    recipe.steps = r.steps;
                    recipeBox.put(recipe);  // add to database

                    for (Ingredient i : recipe.ingredients) {
                        Ingredient ingredient = new Ingredient();
                        ingredient.recipe.setTarget(recipe);
                        ingredient.recipeId = recipe.recipeID;
                        ingredient.ingredient = i.ingredient;
                        ingredient.quantity = i.quantity;
                        ingredient.measure = i.measure;
                        ingredientsBox.put(ingredient);  // add to database
                    }

                    for (Step s : recipe.steps) {
                        Step step = new Step();
                        step.recipe.setTarget(recipe);
                        step.id = s.id;
                        step.recipeId = recipe.recipeID;
                        step.shortDescription = s.shortDescription;
                        step.description = s.description;
                        step.thumbnailURL = s.thumbnailURL;
                        step.videoURL = s.videoURL;
                        stepsBox.put(step);  // add to database
                    }
                }

                //--------------------------------------------------------------------
                // Load the recipes fragment..
                //-----------------------------------------------------------------
                loadFragment();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Toast.makeText(context, "Call to retrieve recipes failed.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void loadFragment(){

        //--------------------------------------------------------------------
        // Clear out anything in the LinearLayout container before refreshing the list of recipes.
        //--------------------------------------------------------------------
        LinearLayout fragment_container = findViewById(R.id.fragment_container);
        fragment_container.removeAllViews();

        //--------------------------------------------------------------------
        // Set up the Fragment Manager..
        //--------------------------------------------------------------------
        FragmentManager fragmentManager = getSupportFragmentManager();
        RecipesListFragment recipesListFragment = new RecipesListFragment();

        //--------------------------------------------------------------------
        // Load the fragment into the layout..
        //--------------------------------------------------------------------
        RecipesListFragment existingFragment = (RecipesListFragment) fragmentManager.findFragmentByTag("recipeList");

        if (existingFragment != null){
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, recipesListFragment, "recipeList")
                    .commit();
        }else{
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_container, recipesListFragment, "recipeList")
                    .commit();
        }
    }

}