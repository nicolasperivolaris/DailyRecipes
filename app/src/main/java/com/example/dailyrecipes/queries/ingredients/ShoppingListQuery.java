package com.example.dailyrecipes.queries.ingredients;

import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.queries.Query;

import org.json.JSONException;

import java.util.List;

public class ShoppingListQuery extends Query<Void, List<Ingredient>> {
    protected ShoppingListQuery(QueryListener<List<Ingredient>> callback) {
        super(callback,Flag.GET_SHOPPING_LIST);
    }

    @Override
    protected List<Ingredient> formatData(String JSON) throws JSONException {
        return null;
    }

    @Override
    public String getArg() {
        return "ShoppingList";
    }
}
