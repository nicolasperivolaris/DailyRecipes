package com.example.dailyrecipes.queries;

import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipeIngredientsQuery extends Query<Void, Recipe>{
    public RecipeIngredientsQuery(Recipe recipe, QueryListener run) {
        super(run);
        setData(recipe);
    }

    @Override
    protected Recipe formatData(String json) throws NumberFormatException, JSONException {
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        //for (int i = 1; i<rawData.size(); i++) //i = 0 => queryId
        //    ingredients.add(toIngredients(rawData.get(i)));

        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            ingredients.add(Ingredient.convertJSON(array.getJSONObject(i)));
        }
        getData().setIngredients(ingredients);

        return getData();
    }

    /*private static Ingredient toIngredients(String s){
        String[] data = s.split(":");
        return new Ingredient(data[1], data[3], Float.parseFloat(data[2]));
    }*/

    @Override
    public int getFlag() {
        return GET_RECIPE;
    }

    @Override
    public String getArg() {
        return String.valueOf(getData().getId());
    }

}
