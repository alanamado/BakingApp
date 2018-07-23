package com.secondfloorapps.bakingapp.models;

import java.util.ArrayList;
import java.util.List;

import io.objectbox.annotation.Backlink;
import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.annotation.Transient;
import io.objectbox.relation.ToMany;

@Entity
public class Recipe {

    @Id
    public long id;
    public long recipeID;
    public String name;
    public String servings;
    public String image;
    @Transient public ArrayList<Ingredient> ingredients;  // @Transient means it won't be stored in the database.  We only need this so the JSON will map correctly to it from RetroFit.
    @Transient public ArrayList<Step> steps;

    // Classes that are the to-many side of a relationships back to this class
    @Backlink  public ToMany<Ingredient> ingredientsList;
    @Backlink  public ToMany<Step> stepsList;

}


