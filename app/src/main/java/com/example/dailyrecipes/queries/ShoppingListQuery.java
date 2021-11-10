package com.example.dailyrecipes.queries;

import com.example.dailyrecipes.model.Ingredient;

import org.json.JSONException;

import java.util.List;

public class ShoppingListQuery extends Query<Void, List<Ingredient>>{
    protected ShoppingListQuery(QueryListener<List<Ingredient>> callback) {
        super(callback);
    }

    @Override
    protected List<Ingredient> formatData(String JSON) throws JSONException {
        return null;
    }

    @Override
    public int getFlag() {
        return GET_SHOPPING_LIST;
    }

    @Override
    public String getArg() {
        return null;
    }
}
