package com.example.dailyrecipes.queries;

import java.util.ArrayList;
import java.util.List;

public class RecipeListQuery extends Query<List<String>> {
    public RecipeListQuery(QueryListener run) {
        super(run);
    }

    @Override
    protected List<String> formatData(ArrayList<String> rawData) {
        List<String> list = new ArrayList<>();
        if(Integer.parseInt(rawData.get(0)) != id) throw new RuntimeException("Id of the query doesn't match with data");
        return rawData.subList(1, rawData.size());
    }

    @Override
    public int getFlag() {
        return GET_RECIPE_LIST;
    }
}
