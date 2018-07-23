package com.secondfloorapps.bakingapp.models;


import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.relation.ToOne;

@Entity
public class Ingredient {

    @Id
    public long uniqueId;
    public long recipeId;
    public String quantity;
    public String measure;
    public String ingredient;

    // Relationship
    public ToOne<Recipe> recipe;

}
