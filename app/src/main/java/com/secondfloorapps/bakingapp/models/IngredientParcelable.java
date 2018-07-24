package com.secondfloorapps.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Ingredient_parc implements Parcelable {
    public long uniqueId;
    public long recipeId;
    public String quantity;
    public String measure;
    public String ingredient;

    public Ingredient_parc(){

    }

    public Ingredient_parc(Parcel in) {
        uniqueId = in.readLong();
        recipeId = in.readLong();
        quantity = in.readString();
        measure = in.readString();
        ingredient = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(uniqueId);
        dest.writeLong(recipeId);
        dest.writeString(quantity);
        dest.writeString(measure);
        dest.writeString(ingredient);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Ingredient_parc> CREATOR = new Parcelable.Creator<Ingredient_parc>() {
        @Override
        public Ingredient_parc createFromParcel(Parcel in) {
            return new Ingredient_parc(in);
        }

        @Override
        public Ingredient_parc[] newArray(int size) {
            return new Ingredient_parc[size];
        }
    };

}
