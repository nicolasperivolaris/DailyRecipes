package com.example.dailyrecipes.model;

import org.json.JSONException;
import org.json.JSONObject;

public abstract class ItemModel {
    protected int id;
    protected String name;

    public ItemModel(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public abstract JSONObject convertToJSON() throws JSONException;
    public int getId(){
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }


    public String getName(){
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
