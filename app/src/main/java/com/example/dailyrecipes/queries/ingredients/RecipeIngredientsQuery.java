package com.example.dailyrecipes.queries.ingredients;

import static com.example.dailyrecipes.queries.Query.Flag.GET_RECIPE_INGREDIENTS;

import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.model.IngredientsFactory;
import com.example.dailyrecipes.model.Recipe;
import com.example.dailyrecipes.queries.Query;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

public class RecipeIngredientsQuery extends Query<Void, Recipe> {
    public RecipeIngredientsQuery(Recipe recipe, QueryListener<Recipe> run) {
        super(run,GET_RECIPE_INGREDIENTS);
        setData(recipe);
    }

    @Override
    protected Recipe formatData(String json) throws NumberFormatException, JSONException {
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            //ingredients.add(IngredientsFactory.instance.convertJSON(array.getJSONObject(i)));
        }
        getData().setIngredients(ingredients);

        return getData();
    }
    @Override
    public String getArg() {
        return String.valueOf(getData().getId());
    }

}
