package com.secondfloorapps.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class IngredientParcelable implements Parcelable {
    public long uniqueId;
    public long recipeId;
    public String quantity;
    public String measure;
    public String ingredient;

    public IngredientParcelable(){

    }

    public IngredientParcelable(Parcel in) {
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
    public static final Parcelable.Creator<IngredientParcelable> CREATOR = new Parcelable.Creator<IngredientParcelable>() {
        @Override
        public IngredientParcelable createFromParcel(Parcel in) {
            return new IngredientParcelable(in);
        }

        @Override
        public IngredientParcelable[] newArray(int size) {
            return new IngredientParcelable[size];
        }
    };

}
