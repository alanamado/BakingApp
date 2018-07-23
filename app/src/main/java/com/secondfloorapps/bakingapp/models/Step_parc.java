package com.secondfloorapps.bakingapp.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Step_parc implements Parcelable {

    public long uniqueId;
    public int id;
    public long recipeId;
    public String shortDescription;
    public String description;
    public String videoURL;
    public String thumbnailURL;

    public Step_parc(){}

    public Step_parc(Parcel in) {
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
    public static final Parcelable.Creator<Step_parc> CREATOR = new Parcelable.Creator<Step_parc>() {
        @Override
        public Step_parc createFromParcel(Parcel in) {
            return new Step_parc(in);
        }

        @Override
        public Step_parc[] newArray(int size) {
            return new Step_parc[size];
        }
    };

}