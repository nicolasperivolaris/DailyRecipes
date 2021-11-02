package com.example.dailyrecipes.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Ingredient {
    private int id;
    private String name;
    private Unit unit;
    private float quantity;

    public Ingredient(int id, String name, Unit unit, float quantity) {
        this.id = id;
        this.name = name;
        this.unit = unit;
        this.quantity = quantity;
    }

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

    public void setId(int id){
        this.id = id;
    }

    public static Ingredient convertJSON(JSONObject jsonObject) throws JSONException{
        int id = jsonObject.getInt("Id");
        String name = jsonObject.getString("Name");
        float quantity = (float) jsonObject.getDouble("Quantity");
        Unit unit = Unit.convertJSON(jsonObject.getJSONObject("Unit"));
        return new Ingredient(id, name, unit, quantity);
    }
}
