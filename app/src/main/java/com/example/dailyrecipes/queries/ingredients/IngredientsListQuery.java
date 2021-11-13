package com.example.dailyrecipes.queries.ingredients;

import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.model.Recipe;
import com.example.dailyrecipes.queries.Query;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class IngredientsListQuery extends Query<Void, List<Ingredient>> {
    public IngredientsListQuery(QueryListener<List<Ingredient>> callback) {
        super(callback);
    }

    @Override
    protected List<Ingredient> formatData(String JSON) throws JSONException {
        List<Ingredient> list = new ArrayList<>();

        JSONArray array = new JSONArray(JSON);
        for (int i = 0; i < array.length(); i++) {
            Ingredient ingredient = Ingredient.convertJSON(array.getJSONObject(i));
            list.add(ingredient);
        }
        return list;
    }

    @Override
    public int getFlag() {
        return GET_INGREDIENT_LIST;
    }

    @Override
    public String getArg() {
        return null;
    }
}
