package com.secondfloorapps.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class StepParcelable implements Parcelable {

    public long uniqueId;
    public int id;
    public long recipeId;
    public String shortDescription;
    public String description;
    public String videoURL;
    public String thumbnailURL;

    public StepParcelable(){}

    public StepParcelable(Parcel in) {
        uniqueId = in.readLong();
        id = in.readInt();
        recipeId = in.readLong();
        shortDescription = in.readString();
        description = in.readString();
        videoURL = in.readString();
        thumbnailURL = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(uniqueId);
        dest.writeInt(id);
        dest.writeLong(recipeId);
        dest.writeString(shortDescription);
        dest.writeString(description);
        dest.writeString(videoURL);
        dest.writeString(thumbnailURL);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<StepParcelable> CREATOR = new Parcelable.Creator<StepParcelable>() {
        @Override
        public StepParcelable createFromParcel(Parcel in) {
            return new StepParcelable(in);
        }

        @Override
        public StepParcelable[] newArray(int size) {
            return new StepParcelable[size];
        }
    };

}