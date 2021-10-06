package com.example.dailyrecipes.queries;

import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.model.Recipe;

import java.util.ArrayList;

public class RecipeQuery extends Query<Recipe>{

    protected RecipeQuery(QueryListener run) {
        super(run);
    }

    @Override
    protected Recipe formatData(ArrayList<String> rawData) {
        if(Integer.parseInt(rawData.get(0)) != id) throw new RuntimeException("Id of the query doesn't match with data");
        String name = rawData.get(1);
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        for (String rawDatum : rawData)
            ingredients.add(toIngredients(rawDatum));

        return new Recipe(name, ingredients);
    }

    private static Ingredient toIngredients(String s){
        String[] data = s.split(":");
        return new Ingredient(data[0], data[1], Float.parseFloat(data[3]));
    }

    @Override
    public int getFlag() {
        return GET_RECIPE;
    }
}
