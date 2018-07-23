package com.secondfloorapps.bakingapp.models;

import java.util.Date;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;


@Entity
public class Widget {

    @Id public long uniqueId;
    public int widgetId;
    public long recipeID;
    public Date lastTouched;
}
