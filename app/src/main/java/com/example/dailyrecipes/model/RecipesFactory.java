package com.example.dailyrecipes.model;

import com.example.dailyrecipes.queries.Query;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Observer;

public class RecipesFactory extends QueryableFactory<Recipe> {
    public static RecipesFactory instance = new RecipesFactory();
    private RecipesFactory(){};

    @Override
    public Query.Flag getFlag() {
        return Query.Flag.GET_RECIPE_LIST;
    }

    public String[] getNames(){
        String[] RecipesName = new String[dataList.size()];
        for (int i = 0; i< dataList.size(); i++) {
            RecipesName[i] = dataList.get(i).getName();
        }
        return RecipesName;
    }

    public Recipe convertJSON(JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt("Id");
        String name = jsonObject.getString("Name");
        String description = jsonObject.getString("Description");
        int multiplier = jsonObject.getInt("Multiplier");
        String imageName = jsonObject.getString("ImagePath");
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        if (!jsonObject.isNull("Ingredients")){
            JSONArray array = jsonObject.getJSONArray("Ingredients");
            for (int i = 0; i < array.length(); i++)
                ingredients.add(IngredientsFactory.instance.convertJSON(array.getJSONObject(i)));
        }
        return new Recipe(id, name,description, ingredients,multiplier, imageName);
    }

    public Recipe newInstance(){
        return new Recipe();
    }
}
