package com.example.dailyrecipes.queries;

import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.model.Recipe;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SaveRecipeQuery extends Query<List<Ingredient>, Integer> {

    private final Recipe recipe;
    public SaveRecipeQuery(QueryListener callback, Recipe recipe) {
        super(callback);
        this.recipe = recipe;
    }

    @Override
    protected Integer formatData(String json) {
        return 0;
    }

    @Override
    public int getFlag() {
        return SET_RECIPE;
    }

    @Override
    public String getArg() {
        //JSONObject toSave = new JSONObject(recipe);
        return null;
    }
}
