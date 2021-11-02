package com.example.dailyrecipes.queries;

import com.example.dailyrecipes.model.Recipe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RecipeListQuery extends Query<Void, List<Recipe>> {
    public RecipeListQuery(QueryListener run) {
        super(run);
    }

    @Override
    protected List<Recipe> formatData(String JSON) throws JSONException {
        List<Recipe> list = new ArrayList<>();

        JSONArray array = new JSONArray(JSON);
        for (int i=0; i<array.length(); i++) {
            Recipe recipe = Recipe.convertJSON(array.getJSONObject(i));
            list.add(recipe);
        }


        //for(int i = 1; i<rawData.size(); i+=3) //reject id's
        //    list.add(new Recipe(Integer.parseInt(rawData.get(i)), rawData.get(i+1), null, rawData.get(i+2)));
        return list;
    }

    @Override
    public int getFlag() {
        return GET_RECIPE_LIST;
    }

    @Override
    public String getArg() {
        return "all";
    }
}
