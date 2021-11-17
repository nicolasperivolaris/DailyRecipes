package com.example.dailyrecipes.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.dailyrecipes.utils.JSONable;

import org.json.JSONException;
import org.json.JSONObject;

public class Ingredient implements Parcelable, JSONable<Ingredient> {
    private int id;
    private String name;
    private Unit unit;
    private float quantity;
    public static final Ingredient EMPTY = new Ingredient(0, "", new Unit(0, "",""), 0);

    Ingredient(int id, String name, Unit unit, float quantity) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
    }

    protected Ingredient(Parcel in) {
        id = in.readInt();
        name = in.readString();
        unit = in.readParcelable(Unit.class.getClassLoader());
        quantity = in.readFloat();
    }

    public static final Creator<Ingredient> CREATOR = new Creator<Ingredient>() {
        @Override
        public Ingredient createFromParcel(Parcel in) {
            return new Ingredient(in);
        }

        @Override
        public Ingredient[] newArray(int size) {
            return new Ingredient[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public float getQuantity() {
        return quantity;
    }

    public void setQuantity(float quantity) {
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    @Override
    public JSONObject convertToJSON() throws JSONException {
        JSONObject result = new JSONObject();
        result.accumulate("Id", id);
        result.accumulate("Name", name);
        result.accumulate("Quantity", quantity);
        result.accumulate("Unit", unit.convertToJSON());
        return result;
    }

    @NonNull
    public Object clone(){
        Ingredient i = new Ingredient(id, name, unit, quantity);
        return i;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeParcelable(unit, flags);
        dest.writeFloat(quantity);
    }
}
