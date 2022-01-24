package com.example.dailyrecipes.queries.ingredients;

import com.example.dailyrecipes.model.Recipe;
import com.example.dailyrecipes.model.RecipesFactory;
import com.example.dailyrecipes.queries.Query;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class ShoppingListQuery extends Query<Void, List<Recipe>> {
    public ShoppingListQuery(QueryListener<List<Recipe>> callback) {
        super(callback,Flag.GET_SHOPPING_LIST);
    }

    @Override
    protected List<Recipe> formatData(String JSON) throws JSONException {
        JSONArray array = new JSONArray(JSON);
        for (int i = 0; i < array.length(); i++) {
            //getData().add(RecipesFactory.instance.convertJSON(array.getJSONObject(i)));
        }

        return getData();
    }

    @Override
    public String getArg() {
        return "ShoppingList";
    }
}
