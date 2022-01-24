package com.example.dailyrecipes.model;

import com.example.dailyrecipes.queries.Query;
import com.example.dailyrecipes.utils.ConnectionManager;

import org.json.JSONException;
import org.json.JSONObject;

public class IngredientsFactory extends QueryableFactory<Ingredient> {
    private final UnitsFactory unitsFactory;
    public IngredientsFactory(ConnectionManager connectionManager){
        super(connectionManager);
        unitsFactory = new UnitsFactory(connectionManager);
    }

    public UnitsFactory getUnitsFactory() {
        return unitsFactory;
    }

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
        Unit unit = unitsFactory.convertJSON(jsonObject.getJSONObject("Unit"));
        return new Ingredient(id, name, unit, quantity);
    }


}
