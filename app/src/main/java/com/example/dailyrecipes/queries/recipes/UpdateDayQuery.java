package com.example.dailyrecipes.queries.recipes;

import com.example.dailyrecipes.model.recipe.Recipe;
import com.example.dailyrecipes.queries.Query;

import org.json.JSONException;
import org.json.JSONObject;

public class UpdateDayQuery extends Query<Recipe, Integer> {

    private final Recipe recipe;

    public UpdateDayQuery(QueryListener<Integer> callback, Recipe recipe) {
        super(callback, Flag.SET_DAY_RECIPE);
        this.recipe = recipe;
    }

    @Override
    protected Integer formatData(String json) {
        return 0;
    }

    @Override
    public String getArg() {
        try {
            JSONObject toSave = recipe.convertToJSON();
            return toSave.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "java error";
    }
}
