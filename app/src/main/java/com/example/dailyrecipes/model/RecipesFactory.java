package com.example.dailyrecipes.model;

import com.example.dailyrecipes.queries.Query;
import com.example.dailyrecipes.queries.recipes.FillRecipeQuery;
import com.example.dailyrecipes.utils.ConnectionManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipesFactory extends QueryableFactory<Recipe> {
    private final IngredientsFactory ingredientsFactory;
    private final DayFactory dayFactory;

    public RecipesFactory(ConnectionManager connectionManager){
        super(connectionManager);
        ingredientsFactory = new IngredientsFactory(connectionManager);
        dayFactory = new DayFactory(connectionManager);

        wait(this);
        wait(ingredientsFactory);
        wait(dayFactory);
        connection.make(new FillRecipeQuery(recipes -> {}, this));
    };

    public IngredientsFactory getIngredientsFactory() {
        return ingredientsFactory;
    }

    public DayFactory getDayFactory() {
        return dayFactory;
    }

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
                ingredients.add(ingredientsFactory.convertJSON(array.getJSONObject(i)));
        }
        Day day = dayFactory.convertJSON(jsonObject.getJSONObject("Day"));

        return new Recipe(id, name,description, ingredients,multiplier, imageName, day);
    }

    public static Recipe newInstance(){
        return new Recipe();
    }

}
