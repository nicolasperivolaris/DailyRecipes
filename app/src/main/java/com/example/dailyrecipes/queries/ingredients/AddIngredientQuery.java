package com.example.dailyrecipes.queries.ingredients;

import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.model.Recipe;
import com.example.dailyrecipes.queries.Query;

import org.json.JSONException;
import org.json.JSONObject;

public class AddIngredientQuery extends Query<Ingredient, Void> {

    private Ingredient ingredient;

    public AddIngredientQuery(Ingredient ingredient, QueryListener<Ingredient> callback) {
        super(callback);
        this.ingredient = ingredient;
    }

    @Override
    protected Void formatData(String JSON) throws JSONException {
        return null;
    }

    @Override
    public int getFlag() {
        return ADD_INGREDIENT;
    }

    @Override
    public String getArg() {
        try {
            JSONObject toSave = ingredient.convertToJSON();
            return toSave.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
