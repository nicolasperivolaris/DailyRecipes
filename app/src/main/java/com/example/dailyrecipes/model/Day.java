package com.example.dailyrecipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Day extends ItemModel implements Parcelable {
    public static final Day NOT_DAY = new Day(-1, "Not planned");

    Day(int id, String name){
        super(id, name);
    }

    protected Day(Parcel in) {
        this(in.readInt(), in.readString());
    }

    public static final Creator<Day> CREATOR = new Creator<Day>() {
        @Override
        public Day createFromParcel(Parcel in) {
            return new Day(in);
        }

        @Override
        public Day[] newArray(int size) {
            return new Day[size];
        }
    };

    @Override
    public JSONObject convertToJSON() throws JSONException {
        JSONObject result = new JSONObject();
        result.accumulate("Id", id);
        result.accumulate("Name", name);
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Day that = (Day) o;
        return that.id == id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
