package com.example.dailyrecipes.queries;

import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.model.Recipe;

import java.util.ArrayList;

public class RecipeQuery extends Query<Recipe>{
    int recipeId;
    public RecipeQuery(int recipeId, QueryListener run) {
        super(run);
        this.recipeId = recipeId;
    }

    @Override
    protected Recipe formatData(ArrayList<String> rawData) throws NumberFormatException{
        if(Integer.parseInt(rawData.get(0)) != id) throw new RuntimeException("Id of the query doesn't match with data");
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        for (String rawDatum : rawData)
            ingredients.add(toIngredients(rawDatum));

        return new Recipe(Integer.parseInt(rawData.get(1)), rawData.get(2), ingredients, rawData.get(3));
    }

    private static Ingredient toIngredients(String s){
        String[] data = s.split(":");
        return new Ingredient(data[0], data[1], Float.parseFloat(data[3]));
    }

    @Override
    public int getFlag() {
        return GET_RECIPE;
    }

    @Override
    public String getArg() {
        return String.valueOf(recipeId);
    }
}
