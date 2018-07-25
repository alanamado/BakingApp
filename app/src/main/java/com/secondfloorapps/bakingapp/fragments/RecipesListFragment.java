package com.secondfloorapps.bakingapp.fragments;


import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.secondfloorapps.bakingapp.MyApp;
import com.secondfloorapps.bakingapp.R;
import com.secondfloorapps.bakingapp.adapters.RecyclerRecipeListAdapter;
import com.secondfloorapps.bakingapp.models.Recipe;

import java.util.List;

import io.objectbox.BoxStore;

public class RecipesListFragment extends Fragment {

    RecyclerView recyclerView;
    Context context;
    RecyclerRecipeListAdapter adapter;
    List<Recipe> recipeList;
    GridLayoutManager gridLayoutManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        // Inflate the fragment layout
        final View rootView = inflater.inflate(R.layout.fragment_recipe_list, container,false);

        // reference recyclerview
        recyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_recipes);

        //-----------------------------------------------------
        // Set up ObjectBox Boxstore(s)..
        //-----------------------------------------------------
        MyApp myapp = (MyApp) context.getApplicationContext();
        BoxStore boxStore = myapp.getBoxStore();
        List<Recipe> recipeList = boxStore.boxFor(Recipe.class).getAll();

        //-----------------------------------------------------
        // Set up recyclerview..
        //-----------------------------------------------------
        adapter = new RecyclerRecipeListAdapter(recipeList);
        recyclerView.setAdapter(adapter);

        //-----------------------------------------------------
        // Set up GridLayoutManager and add as recyclerview's layout...
        //-----------------------------------------------------
        if (this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            gridLayoutManager = new GridLayoutManager(context, 1);  // for portrait mode

        } else {
            gridLayoutManager = new GridLayoutManager(context, 4);  // for landscape mode
        }

         recyclerView.setLayoutManager(gridLayoutManager);


        return rootView;
    }


}
