package com.example.dailyrecipes.model;

import android.net.Uri;

import androidx.lifecycle.ViewModelProvider;

import com.example.dailyrecipes.MainActivity;
import com.example.dailyrecipes.queries.Query;
import com.example.dailyrecipes.queries.recipes.FillRecipesQuery;
import com.example.dailyrecipes.utils.ConnectionManager;
import com.example.dailyrecipes.utils.FTPManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RecipesFactory extends QueryableFactory<Recipe> {
    private final IngredientsFactory ingredientsFactory;
    private final DayFactory dayFactory;

    public RecipesFactory(ConnectionManager connectionManager){
        super(connectionManager);
        ingredientsFactory = new IngredientsFactory(connectionManager);
        dayFactory = new DayFactory(connectionManager);

        dayFactory.update();
        ingredientsFactory.update();
        update();
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    protected void onUpdated() {
        connection.make(new FillRecipesQuery(recipes -> {}, this));
    }

    @Override
    protected void waitDependencies() {
        wait(dayFactory);
        wait(ingredientsFactory);
    }

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
        Uri img = (new ViewModelProvider(MainActivity.instance).get(FTPManager.class)).getFile(imageName);
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        if (!jsonObject.isNull("Ingredients")){
            JSONArray array = jsonObject.getJSONArray("Ingredients");
            for (int i = 0; i < array.length(); i++)
                ingredients.add(ingredientsFactory.convertJSON(array.getJSONObject(i)));
        }
        Day day = dayFactory.convertJSON(jsonObject.getJSONObject("Day"));

        return new Recipe(id, name,description, ingredients,multiplier, img, imageName, day);
    }

    public static Recipe newInstance(){
        return new Recipe();
    }

}
