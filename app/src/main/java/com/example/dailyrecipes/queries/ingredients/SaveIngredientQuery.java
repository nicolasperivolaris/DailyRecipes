package com.example.dailyrecipes.queries.ingredients;

import static com.example.dailyrecipes.queries.Query.Flag.SAVE_INGREDIENT;

import com.example.dailyrecipes.model.Ingredient;
import com.example.dailyrecipes.model.ItemModel;
import com.example.dailyrecipes.queries.Query;

import org.json.JSONException;
import org.json.JSONObject;

public class SaveIngredientQuery extends Query<Ingredient, Void> {

    private Ingredient i;

    public SaveIngredientQuery(Ingredient i, QueryListener<Void> callback) {
        super(callback, SAVE_INGREDIENT);
        this.i = i;
    }

    @Override
    protected Void formatData(String JSON) throws JSONException {
        return null;
    }

    @Override
    public String getArg() {
        try {
            JSONObject toSave = i.convertToJSON();
            return toSave.toString();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "java error";
    }
}
