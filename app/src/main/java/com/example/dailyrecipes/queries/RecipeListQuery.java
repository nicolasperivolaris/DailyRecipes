package com.example.dailyrecipes.queries;

import com.example.dailyrecipes.model.Recipe;

import java.util.ArrayList;
import java.util.List;

public class RecipeListQuery extends Query<List<Recipe>> {
    public RecipeListQuery(QueryListener run) {
        super(run);
    }

    @Override
    protected List<Recipe> formatData(ArrayList<String> rawData) {
        List<Recipe> list = new ArrayList<>();
        if(Integer.parseInt(rawData.get(0)) != id) throw new RuntimeException("Id of the query doesn't match with data");
        for(int i = 1; i<rawData.size(); i+=3) //reject id's
            list.add(new Recipe(Integer.parseInt(rawData.get(i)), rawData.get(i+1), null, rawData.get(i+2)));
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
