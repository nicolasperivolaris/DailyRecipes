package com.example.dailyrecipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

public class Unit implements Parcelable {
    private int id;
    private String name;
    private String symbol;

    public Unit(int id, String name, String symbol) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
    }

    protected Unit(Parcel in) {
        id = in.readInt();
        name = in.readString();
        symbol = in.readString();
    }

    public static final Creator<Unit> CREATOR = new Creator<Unit>() {
        @Override
        public Unit createFromParcel(Parcel in) {
            return new Unit(in);
        }

        @Override
        public Unit[] newArray(int size) {
            return new Unit[size];
        }
    };

    public static Unit convertJSON(JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt("Id");
        String name = jsonObject.getString("Name");
        String symbol = jsonObject.getString("Symbol");
        return new Unit(id, name, symbol);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public Object convertToJSON() throws JSONException {
        JSONObject result = new JSONObject();
        result.accumulate("Id", id);
        result.accumulate("Name", name);
        result.accumulate("Symbol", symbol);
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
        dest.writeString(symbol);
    }
}

