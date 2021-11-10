package com.example.dailyrecipes.queries;

import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.model.Recipe;

import org.json.JSONException;
import org.json.JSONObject;

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
        try {
            JSONObject toSave = recipe.convertToJSON();
            return toSave.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
