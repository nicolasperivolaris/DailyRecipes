package com.example.dailyrecipes.model;

import com.example.dailyrecipes.queries.Query;

import org.json.JSONException;
import org.json.JSONObject;

public class IngredientsFactory extends QueryableFactory<Ingredient>{
    public static IngredientsFactory instance = new IngredientsFactory();

    @Override
    public Query.Flag getFlag() {
        return Query.Flag.GET_INGREDIENT_LIST;
    }

    public String[] getNames(){
        String[] ingredientsNames = new String[dataList.size()];
        for (int i = 0; i<dataList.size(); i++) {
            ingredientsNames[i] = dataList.get(i).getName();
        }
        return ingredientsNames;
    }

    public Ingredient convertJSON(JSONObject jsonObject) throws JSONException {
        int id = jsonObject.getInt("Id");
        String name = jsonObject.getString("Name");
        float quantity = (float) jsonObject.getDouble("Quantity");
        Unit unit = UnitsFactory.instance.convertJSON(jsonObject.getJSONObject("Unit"));
        return new Ingredient(id, name, unit, quantity);
    }


}
