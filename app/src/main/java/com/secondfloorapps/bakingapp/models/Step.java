package com.secondfloorapps.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;
import io.objectbox.annotation.Index;
import io.objectbox.relation.ToOne;

@Entity
public class Step {

    @Id public long uniqueId;
    public int id;
    @Index public long recipeId;
    public String shortDescription;
    public String description;
    public String videoURL;
    public String thumbnailURL;

    // Relationship
    public ToOne<Recipe> recipe;


}