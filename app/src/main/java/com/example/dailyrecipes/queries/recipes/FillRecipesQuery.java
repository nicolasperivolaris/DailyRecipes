package com.example.dailyrecipes.queries.recipes;

import static com.example.dailyrecipes.queries.Query.Flag.GET_RECIPE_AND_ALL;

import com.example.dailyrecipes.model.DayFactory;
import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.model.IngredientsFactory;
import com.example.dailyrecipes.model.Recipe;
import com.example.dailyrecipes.model.RecipesFactory;
import com.example.dailyrecipes.model.Unit;
import com.example.dailyrecipes.model.UnitsFactory;
import com.example.dailyrecipes.queries.Query;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class FillRecipesQuery extends Query<Void, List<Recipe>> {
    private final RecipesFactory recipesFactory;
    public FillRecipesQuery(QueryListener<List<Recipe>> run, RecipesFactory recipesFactory) {
        super(run,GET_RECIPE_AND_ALL);
        this.recipesFactory = recipesFactory;
    }

    @Override
    protected List<Recipe> formatData(String json) throws NumberFormatException, JSONException {
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) { //row == recipe_id,ingredient_id,quantity, unit_id
            JSONArray line = array.getJSONArray(i);
            Integer recipe_id = line.getInt(0);
            Integer ingredient_id = line.getInt(1);
            int quantity = line.getInt(2);
            Integer unit_id = line.getInt(3);

            Recipe recipe = recipesFactory.getDataList().get(recipe_id);
            Ingredient ingredient = (Ingredient) recipesFactory.getIngredientsFactory().getDataList().get(ingredient_id).clone();
            ingredient.setQuantity(quantity);
            Unit unit = recipesFactory.getIngredientsFactory().getUnitsFactory().getDataList().get(unit_id);

            recipe.getIngredients().put(ingredient_id, ingredient);
            ingredient.setUnit(unit);
        }

        return new ArrayList<Recipe>(recipesFactory.getDataList().values());
    }
    @Override
    public String getArg() {
        return String.valueOf("Recipe_has_ingredient");
    }

}
