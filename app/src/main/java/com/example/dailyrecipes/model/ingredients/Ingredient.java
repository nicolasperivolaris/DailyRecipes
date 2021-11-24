package com.example.dailyrecipes.model.ingredients;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.dailyrecipes.model.ItemModel;
import com.example.dailyrecipes.model.unit.Unit;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class Ingredient extends ItemModel implements Parcelable{
    private Unit unit;
    private float quantity;
    public static final Ingredient EMPTY = new Ingredient(0, "", Unit.EMPTY, 0);

    Ingredient(int id, String name, Unit unit, float quantity) {
        super(id, name);
        this.unit = unit;
        this.quantity = quantity;
    }

    protected Ingredient(Parcel in) {
        super(in.readInt(),in.readString());
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return that.id == id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(unit, quantity);
    }
}