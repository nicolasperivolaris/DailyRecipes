package com.example.dailyrecipes.queries.recipes;

import com.example.dailyrecipes.model.Recipe;
import com.example.dailyrecipes.queries.Query;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class RecipeListQuery extends Query<Void, List<Recipe>> {
    public RecipeListQuery(QueryListener<List<Recipe>> run) {
        super(run);
    }

    @Override
    protected List<Recipe> formatData(String JSON) throws JSONException {
        List<Recipe> list = new ArrayList<>();

        JSONArray array = new JSONArray(JSON);
        for (int i = 0; i < array.length(); i++) {
            Recipe recipe = Recipe.convertJSON(array.getJSONObject(i));
            list.add(recipe);
        }
        return list;
    }

    @Override
    public int getFlag() {
        return GET_RECIPE_LIST;
    }

    @Override
    public String getArg() {
        return "all";
    }
}
